package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.model.Bank;
import qiwi.model.Credit;
import qiwi.model.input.CreditEditInput;
import qiwi.model.input.CreditInput;
import qiwi.util.Validator;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/credits")
public class CreditController {
    @Autowired
    private CreditDAO creditDAO;
    @Autowired
    private BankDAO bankDAO;

    private void setUpView(Model model, CreditInput input, CreditEditInput creditEditInput) {
        model.addAttribute("credits", creditDAO.findAll());
        model.addAttribute("creditInput", input);
        model.addAttribute("creditEditInput", creditEditInput);
    }

    private void setUpViewAndAddAttribute(String attribute, Model model, CreditInput input, CreditEditInput creditEditInput) {
        setUpView(model, input, creditEditInput);
        model.addAttribute(attribute, "");
    }

    private Credit updateCredit(Credit credit, CreditInput input) {
        Credit updatedCredit = new Credit();

        if (input.getLimit() != null && !input.getLimit().isEmpty()) {
            updatedCredit.setLimit(BigDecimal.valueOf(Double.parseDouble(input.getLimit())));
        } else {
            updatedCredit.setLimit(credit.getLimit());
        }

        if (input.getInterest() != null && !input.getInterest().isEmpty()) {
            updatedCredit.setInterest(BigDecimal.valueOf(Double.parseDouble(input.getInterest())));
        } else {
            updatedCredit.setInterest(credit.getInterest());
        }

        return updatedCredit;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditInput") CreditInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsCreditMessage", model, input, new CreditEditInput());
            return "credits";
        }

        if (!Validator.Credit.isValid(input)) {
            setUpViewAndAddAttribute("invalidFieldsCreditMessage", model, input, new CreditEditInput());
            return "credits";
        }

        boolean hasErrors = false;

        BigDecimal limit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));
        if (limit.compareTo(BigDecimal.ZERO) != 1) {
            setUpViewAndAddAttribute("illegalLimitValueMessage", model, input, new CreditEditInput());
            hasErrors = true;
        }

        BigDecimal interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));
        if (interest.compareTo(BigDecimal.ZERO) == -1) {
            setUpViewAndAddAttribute("illegalInterestValueMessage", model, input, new CreditEditInput());
            hasErrors = true;
        }

        if (hasErrors) {
            return "credits";
        }

        Bank bank = bankDAO.getBankByName(input.getBank());
        Credit credit = new Credit(input, bank);

        if (bank != null) {
            if (creditDAO.exists(credit)) {
                setUpViewAndAddAttribute("alreadyExistsCreditMessage", model, input, new CreditEditInput());
                return "credits";
            }

            bank.addCredit(credit);
            creditDAO.add(credit);
        } else {
            setUpViewAndAddAttribute("nonExistentBankMessage", model, input, new CreditEditInput());
            return "credits";
        }

        return "redirect:/credits/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("creditEditInput") CreditEditInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyBankEditCreditMessage", model, new CreditInput(), input);
            return "credits";
        }

        if (!Validator.Credit.isValidEdit(input)) {
            setUpViewAndAddAttribute("invalidFieldsCreditMessage", model, new CreditInput(), input);
            return "credits";
        }

        boolean hasErrors = false;

        BigDecimal newLimit = BigDecimal.valueOf(Double.parseDouble(input.getNewLimit()));
        if (!input.getNewLimit().isEmpty()) {
            if (newLimit.compareTo(BigDecimal.ZERO) != 1) {
                setUpViewAndAddAttribute("illegalLimitValueMessage", model, new CreditInput(), input);
                hasErrors = true;
            }
        }

        BigDecimal newInterest = BigDecimal.valueOf(Double.parseDouble(input.getNewInterest()));
        if (!input.getNewInterest().isEmpty()) {
            if (newInterest.compareTo(BigDecimal.ZERO) == -1) {
                setUpViewAndAddAttribute("illegalInterestValueMessage", model, new CreditInput(), input);
                hasErrors = true;
            }
        }

        if (hasErrors) {
            return "credits";
        }

        BigDecimal limit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));
        BigDecimal interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));

        Credit credit = creditDAO.getCredit(input.getBank(), limit, interest);
        Credit updatedCredit = updateCredit(credit, input);

        if (!creditDAO.exists(updatedCredit)) {
            credit.setLimit(updatedCredit.getLimit());
            credit.setInterest(updatedCredit.getInterest());
        } else {
            setUpViewAndAddAttribute("alreadyExistsEditCreditMessage", model, new CreditInput(), input);
            return "credits";
        }

        return "redirect:/credits/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        creditDAO.deleteById(id);
        return "redirect:/credits/";
    }

    @GetMapping("/")
    public String showAllCredits(Model model) {
        setUpView(model, new CreditInput(), new CreditEditInput());
        return "credits";
    }
}
