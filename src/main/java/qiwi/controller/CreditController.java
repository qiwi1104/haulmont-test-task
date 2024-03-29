package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import qiwi.model.Bank;
import qiwi.model.Credit;
import qiwi.model.CreditOffer;
import qiwi.service.BankService;
import qiwi.service.CreditService;
import qiwi.util.CreditValidator;
import qiwi.util.StringUtil;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@SessionAttributes(types = UUID.class, names = "bankId")
@RequestMapping("/credits")
public class CreditController {
    @Autowired
    private CreditService creditDAO;
    @Autowired
    private BankService bankDAO;

    private void setUpModel(Model model) {
        model.addAttribute("credits", creditDAO.findAll());
        model.addAttribute("banks", bankDAO.findAll());
        model.addAttribute("stringUtil", new StringUtil());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("credit") @Valid Credit credit, BindingResult result, Model model) {
        CreditValidator validator = new CreditValidator();
        validator.validate(credit, result);

        if (result.hasErrors()) {
            setUpModel(model);
            return "credit/credits";
        }

        if (creditDAO.exists(credit)) {
            setUpModel(model);
            result.reject("alreadyExists", "This credit already exists.");
            return "credit/credits";
        }

        creditDAO.add(credit);

        return "redirect:/credits/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("credit", creditDAO.getCreditById(id));
        model.addAttribute("bankId", creditDAO.getCreditById(id).getBank().getId());

        return "credit/credit-edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("credit") @Valid Credit credit, BindingResult result,
                       @SessionAttribute("bankId") UUID id, SessionStatus status) {

        credit.setBank(bankDAO.getBankById(id));

        for (Bank bank : bankDAO.findAll()) {
            for (CreditOffer creditOffer : bank.getCreditOffers()) {
                if (creditOffer.getCredit().equals(creditDAO.getCreditById(credit.getId()))) {
                    result.reject("unavailableCredit", "Cannot edit credit which is used in a credit offer.");
                    return "credit/credit-edit";
                }
            }
        }

        CreditValidator validator = new CreditValidator();
        validator.validate(credit, result);

        if (result.hasErrors()) {
            return "credit/credit-edit";
        }

        if (creditDAO.exists(credit)) {
            result.reject("alreadyExists", "This credit already exists.");
            return "credit/credit-edit";
        }

        creditDAO.update(creditDAO.getCreditById(credit.getId()), credit);

        status.setComplete();

        return "redirect:/credits/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        creditDAO.deleteById(id);
        return "redirect:/credits/";
    }

    @GetMapping("/")
    public String showAllCredits(Model model) {
        setUpModel(model);
        model.addAttribute("credit", new Credit());

        return "credit/credits";
    }
}
