package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.CreditDAO;
import qiwi.model.Credit;
import qiwi.model.input.CreditInput;
import qiwi.util.Validator;

import java.util.UUID;

@Controller
@RequestMapping("/credits")
public class CreditController {
    @Autowired
    private CreditDAO creditDAO;

    private void setUpView(Model model, CreditInput input) {
        model.addAttribute("credits", creditDAO.findAll());
        model.addAttribute("creditInput", input);
    }

    private boolean updateCredit(Credit credit, CreditInput input) {
        Credit updatedCredit = new Credit();

        if (input.getLimit() != null && !input.getLimit().isEmpty()) {
            updatedCredit.setLimit(Double.parseDouble(input.getLimit()));
        }

        if (input.getInterest() != null && !input.getInterest().isEmpty()) {
            updatedCredit.setInterest(Double.parseDouble(input.getInterest()));
        }

        if (!creditDAO.exists(updatedCredit)) {
            credit.setLimit(updatedCredit.getLimit());
            credit.setInterest(updatedCredit.getInterest());
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditInput") CreditInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpView(model, input);
            model.addAttribute("emptyFieldsCreditMessage", "");
            return "credits";
        }

        if (!Validator.isValid(input)) {
            setUpView(model, input);
            model.addAttribute("invalidFieldsCreditMessage", "");
            return "credits";
        }

        Credit credit = new Credit(input);
        if (creditDAO.exists(credit)) {
            setUpView(model, input);
            model.addAttribute("alreadyExistsCreditMessage", "");
            return "credits";
        }

        creditDAO.add(credit);

        return "redirect:/credits/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("creditInput") CreditInput input, Model model) {
        if (input.getId().isEmpty()) {
            setUpView(model, input);
            model.addAttribute("emptyIdCreditMessage", "");
            return "credits";
        }

        if (!Validator.isValidEdit(input)) {
            setUpView(model, input);
            model.addAttribute("invalidFieldsCreditMessage", "");
            return "credits";
        }

        Credit credit = creditDAO.getCreditById(UUID.fromString(input.getId()));
        if (updateCredit(credit, input)) {
            creditDAO.add(credit);
        } else {
            setUpView(model, input);
            model.addAttribute("alreadyExistsCreditMessage", "");
            return "credits";
        }

        return "redirect:/credits/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        creditDAO.delete(id);
        return "redirect:/credits/";
    }

    @GetMapping("/")
    public String showAllCredits(Model model) {
        setUpView(model, new CreditInput());
        return "credits";
    }
}
