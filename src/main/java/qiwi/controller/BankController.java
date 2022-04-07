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
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.Credit;
import qiwi.util.CreditValidator;
import qiwi.util.StringUtil;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@SessionAttributes(types = UUID.class, names = "bankId")
@RequestMapping("/banks")
public class BankController {
    @Autowired
    private BankDAO bankDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private CreditDAO creditDAO;

    private void setUpView(Model model) {
        model.addAttribute("banks", bankDAO.findAll());
        model.addAttribute("stringUtil", new StringUtil());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("bank") @Valid Bank bank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setUpView(model);
            return "bank/banks";
        }

        if (bankDAO.existsByName(bank.getName())) {
            setUpView(model);
            result.reject("error.alreadyExists", "This bank already exists.");
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

    @PostMapping("/update")
    public String update(@ModelAttribute("bank") @Valid Bank bank, BindingResult result) {
        if (result.hasErrors()) {
            return "bank/bank-edit";
        }

        if (bankDAO.existsByName(bank.getName())) {
            result.reject("error.alreadyExists", "This bank already exists.");
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
                result.reject("error.alreadyExists", "This person is already a client of this bank.");
                return "bank/add-client";
            }

            bank.addClient(clientDAO.getClientByPassport(client.getPassport()));
            bankDAO.add(bank);
        } else {
            result.reject("error.nonExistentClient", "This person doesn't exist.");
            return "bank/add-client";
        }

        status.setComplete();

        return "redirect:/banks/";
    }

    @GetMapping("add-credit/{id}")
    public String addCredit(@PathVariable UUID id, Model model) {
        model.addAttribute("bankId", id);
        model.addAttribute("credit", new Credit());

        return "bank/add-credit";
    }

    @PostMapping("/add-credit")
    public String addCredit(@ModelAttribute("credit") @Valid Credit credit, BindingResult result,
                            @SessionAttribute("bankId") UUID id, SessionStatus status) {
        CreditValidator validator = new CreditValidator();
        validator.validate(credit, result);

        if (result.hasErrors()) {
            return "bank/add-credit";
        }

        Bank bank = bankDAO.getBankById(id);
        credit.setBank(bank);

        if (creditDAO.exists(credit)) {
            result.reject("alreadyExists", "This credit already exists.");
            return "bank/add-credit";
        }

        bank.addCredit(credit);
        creditDAO.add(credit);

        status.setComplete();

        return "redirect:/banks/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        bankDAO.deleteById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable UUID id) {
        bankDAO.deleteClientById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/deleteCredit/{id}")
    public String deleteCredit(@PathVariable UUID id) {
        creditDAO.deleteById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/")
    public String showAllBanks(Model model) {
        setUpView(model);

        model.addAttribute("bank", new Bank());
        return "bank/banks";
    }
}
