package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.dao.impl.CreditOfferDAO;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.CreditOffer;
import qiwi.util.CreditOfferValidator;
import qiwi.util.StringUtil;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@SessionAttributes(types = {UUID.class, String.class},
        names = {"bankId", "passport"})
@RequestMapping("/banks")
public class BankController {
    @Autowired
    private BankDAO bankDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private CreditDAO creditDAO;
    @Autowired
    private CreditOfferDAO creditOfferDAO;

    private void setUpModel(Model model) {
        model.addAttribute("banks", bankDAO.findAll());
        model.addAttribute("stringUtil", new StringUtil());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("bank") @Valid Bank bank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setUpModel(model);
            return "bank/banks";
        }

        if (bankDAO.existsByName(bank.getName())) {
            setUpModel(model);
            result.reject("alreadyExists", "This bank already exists.");
            return "bank/banks";
        }

        bankDAO.add(bank);

        return "redirect:/banks/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("bank", bankDAO.getBankById(id));

        return "bank/bank-edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("bank") @Valid Bank bank, BindingResult result) {
        if (result.hasErrors()) {
            return "bank/bank-edit";
        }

        if (bankDAO.existsByName(bank.getName())) {
            result.reject("alreadyExists", "This bank already exists.");
            return "bank/bank-edit";
        }

        bankDAO.update(bankDAO.getBankById(bank.getId()), bank);

        return "redirect:/banks/";
    }

    @GetMapping("/add-client/{id}")
    public String addClient(@PathVariable UUID id, Model model) {
        model.addAttribute("bankId", id);
        model.addAttribute("client", new Client());

        return "bank/add-client";
    }

    @PostMapping("/add-client")
    public String addClient(@Valid Client client, BindingResult result,
                            @SessionAttribute("bankId") UUID id, SessionStatus status) {
        if (result.hasErrors()) {
            return "bank/add-client";
        }

        if (clientDAO.existsByPassport(client.getPassport())) {
            Bank bank = bankDAO.getBankById(id);

            if (bankDAO.existsClientByPassport(bank.getName(), client.getPassport())) {
                result.reject("alreadyExists", "This person is already a client of this bank.");
                return "bank/add-client";
            }

            bank.addClient(clientDAO.getClientByPassport(client.getPassport()));
            bankDAO.add(bank);
        } else {
            result.reject("nonExistentClient", "This person doesn't exist.");
            return "bank/add-client";
        }

        status.setComplete();

        return "redirect:/banks/";
    }

    @GetMapping("/add-credit-offer/{id}/{passport}")
    public String addCreditOffer(@PathVariable UUID id, @PathVariable String passport, Model model) {
        model.addAttribute("bankId", id);
        model.addAttribute("passport", passport);

        model.addAttribute("bank", bankDAO.getBankById(id));
        model.addAttribute("creditOffer", new CreditOffer());

        return "bank/add-credit-offer";
    }

    @PostMapping("/add-credit-offer")
    public String addCreditOffer(@ModelAttribute("creditOffer") @Valid CreditOffer creditOffer, BindingResult result,
                                 @ModelAttribute("months") Integer months,
                                 @SessionAttribute("bankId") UUID id, @SessionAttribute("passport") String passport,
                                 Model model, SessionStatus status) {

        CreditOfferValidator validator = new CreditOfferValidator();
        validator.validate(creditOffer, result, months);

        if (result.hasErrors()) {
            setUpModel(model);
            model.addAttribute("bank", bankDAO.getBankById(id));

            return "bank/add-credit-offer";
        }

        creditOffer.calculatePayments(months);

        creditOffer.setClient(clientDAO.getClientByPassport(passport));
        creditOffer.setBank(bankDAO.getBankById(id));

        if (creditOfferDAO.exists(creditOffer)) {
            setUpModel(model);
            model.addAttribute("bank", bankDAO.getBankById(id));

            result.reject("alreadyExists", "This credit offer already exists.");

            return "bank/add-credit-offer";
        }

        creditOfferDAO.add(creditOffer);

        status.setComplete();

        return "redirect:/banks/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        bankDAO.deleteById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/delete-client/{id}")
    public String deleteClient(@PathVariable UUID id) {
        bankDAO.deleteClientById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/delete-credit/{id}")
    public String deleteCredit(@PathVariable UUID id) {
        creditDAO.deleteById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/")
    public String showAllBanks(Model model) {
        setUpModel(model);
        model.addAttribute("bank", new Bank());

        return "bank/banks";
    }
}
