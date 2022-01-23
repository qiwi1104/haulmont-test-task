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

    private void updateCredit(Credit credit, CreditEditInput input) {
        if (input.getNewLimit() != null && !input.getNewLimit().isEmpty()) {
            credit.setLimit(BigDecimal.valueOf(Double.parseDouble(input.getNewLimit())));
        } else {
            credit.setLimit(BigDecimal.valueOf(Double.parseDouble(input.getLimit())));
        }

        if (input.getNewInterest() != null && !input.getNewInterest().isEmpty()) {
            credit.setInterest(BigDecimal.valueOf(Double.parseDouble(input.getNewInterest())));
        } else {
            credit.setInterest(BigDecimal.valueOf(Double.parseDouble(input.getInterest())));
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditInput") CreditInput input, Model model) {
        boolean hasErrors = false;

        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsCreditMessage", model, input, new CreditEditInput());
            return "credits";
        }

        if (!bankDAO.existsByName(input.getBank())) {
            setUpViewAndAddAttribute("nonExistentBankCreditMessage", model, input, new CreditEditInput());
            hasErrors = true;
        }

        if (!input.getLimit().isEmpty()) {
            try {
                BigDecimal limit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));

                if (limit.compareTo(BigDecimal.ZERO) != 1) {
                    setUpViewAndAddAttribute("limitErrorMessage", model, input, new CreditEditInput());
                    hasErrors = true;
                }
            } catch (Exception e) {
                setUpViewAndAddAttribute("invalidFieldsCreditMessage", model, input, new CreditEditInput());
                hasErrors = true;
            }
        }

        if (!input.getInterest().isEmpty()) {
            try {
                BigDecimal interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));

                if (interest.compareTo(BigDecimal.ZERO) == -1) {
                    setUpViewAndAddAttribute("interestErrorMessage", model, input, new CreditEditInput());
                    hasErrors = true;
                }
            } catch (Exception e) {
                setUpViewAndAddAttribute("invalidFieldsCreditMessage", model, input, new CreditEditInput());
                hasErrors = true;
            }
        }

        if (hasErrors) {
            return "credits";
        }

        Bank bank = bankDAO.getBankByName(input.getBank());
        Credit credit = new Credit(bank, input);

        if (creditDAO.exists(credit)) {
            setUpViewAndAddAttribute("alreadyExistsCreditMessage", model, input, new CreditEditInput());
            return "credits";
        }

        bank.addCredit(credit);
        creditDAO.add(credit);

        return "redirect:/credits/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("creditEditInput") CreditEditInput input, Model model) {
        boolean hasErrors = false;

        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsCreditEditMessage", model, new CreditInput(), input);
            hasErrors = true;
        }

        if (hasErrors) {
            return "credits";
        }

        BigDecimal limit = BigDecimal.ZERO;
        if (!input.getLimit().isEmpty()) {
            try {
                limit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));

                if (limit.compareTo(BigDecimal.ZERO) != 1) {
                    setUpViewAndAddAttribute("limitEditErrorMessage", model, input, new CreditEditInput());
                    hasErrors = true;
                }
            } catch (Exception e) {
                setUpViewAndAddAttribute("invalidFieldsEditCreditMessage", model, input, new CreditEditInput());
                hasErrors = true;
            }
        }

        BigDecimal interest = BigDecimal.ZERO;
        if (!input.getInterest().isEmpty()) {
            try {
                interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));

                if (interest.compareTo(BigDecimal.ZERO) == -1) {
                    setUpViewAndAddAttribute("interestEditErrorMessage", model, input, new CreditEditInput());
                    hasErrors = true;
                }
            } catch (Exception e) {
                setUpViewAndAddAttribute("invalidFieldsEditCreditMessage", model, input, new CreditEditInput());
                hasErrors = true;
            }
        }

        BigDecimal newLimit;
        if (!input.getNewLimit().isEmpty()) {
            try {
                newLimit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));

                if (newLimit.compareTo(BigDecimal.ZERO) != 1) {
                    setUpViewAndAddAttribute("limitEditErrorMessage", model, input, new CreditEditInput());
                    hasErrors = true;
                }
            } catch (Exception e) {
                setUpViewAndAddAttribute("invalidFieldsEditCreditMessage", model, input, new CreditEditInput());
                hasErrors = true;
            }
        }

        BigDecimal newInterest;
        if (!input.getNewInterest().isEmpty()) {
            try {
                newInterest = BigDecimal.valueOf(Double.parseDouble(input.getNewInterest()));

                if (newInterest.compareTo(BigDecimal.ZERO) == -1) {
                    setUpViewAndAddAttribute("interestEditErrorMessage", model, new CreditInput(), input);
                    hasErrors = true;
                }
            } catch (Exception e) {
                setUpViewAndAddAttribute("invalidFieldsEditCreditMessage", model, new CreditInput(), input);
                hasErrors = true;
            }
        }

        if (!input.getBank().isEmpty()) {
            if (!bankDAO.existsByName(input.getBank())) {
                setUpViewAndAddAttribute("nonExistentBankEditCreditMessage", model, new CreditInput(), input);
                hasErrors = true;
            }
        }

        if (hasErrors) {
            return "credits";
        }

        if (!creditDAO.exists(new Credit(bankDAO.getBankByName(input.getBank()), input))) {
            setUpViewAndAddAttribute("nonExistentEditCreditMessage", model, new CreditInput(), input);
            return "credits";
        }

        Credit updatedCredit = new Credit(bankDAO.getBankByName(input.getBank()), limit, interest);
        updateCredit(updatedCredit, input);

        if (!creditDAO.exists(updatedCredit)) {
            Credit credit = creditDAO.getCredit(input.getBank(), limit, interest);
            credit.setLimit(updatedCredit.getLimit());
            credit.setInterest(updatedCredit.getInterest());

            bankDAO.getBankByName(input.getBank()).addCredit(credit);
            creditDAO.add(credit);
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
