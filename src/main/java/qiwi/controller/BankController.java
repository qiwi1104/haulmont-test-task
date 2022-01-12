package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.input.BankInput;
import qiwi.model.input.ClientInput;
import qiwi.model.input.CreditInput;
import qiwi.util.Validator;

import java.util.UUID;

@Controller
@RequestMapping("/banks")
public class BankController {
    @Autowired
    private BankDAO bankDAO;
    @Autowired
    private ClientDAO clientDAO;

    private void setUpView(Model model, ClientInput clientInput, BankInput bankInput) {
        model.addAttribute("banks", bankDAO.findAll());
        model.addAttribute("clientInput", clientInput);
        model.addAttribute("bankInput", bankInput);
    }

    @PostMapping("/add")
    public String add(@ModelAttribute BankInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpView(model, new ClientInput(), input);
            model.addAttribute("emptyFieldsMessage", "");
            return "banks";
        }

        Bank bank = new Bank(input);
        if (bankDAO.exists(bank)) {
            setUpView(model, new ClientInput(), input);
            model.addAttribute("alreadyExistsMessage", "");
            return "banks";
        }

        bankDAO.add(bank);

        return "redirect:/banks/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute BankInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpView(model, new ClientInput(), input);
            model.addAttribute("emptyFieldsBankMessage", "");
            return "banks";
        }

        Bank bank = bankDAO.getBankByName(input.getName());
        if (bankDAO.getBankByName(input.getNewName()) != null) {
            setUpView(model, new ClientInput(), input);
            model.addAttribute("alreadyExistsBankNameMessage", "");
            return "banks";
        }

        bank.setName(input.getNewName());
        bankDAO.add(bank);

        return "redirect:/banks/";
    }

    @PostMapping("/addClient")
    public String addClient(@ModelAttribute ClientInput input, Model model) {
        if (input.hasEmptyFields()) {
            Client client = new Client(input);

            if (clientDAO.existsByPassport(client.getPassport())) {
                if (!input.getPassport().isEmpty()) {
                    if (bankDAO.exists(input.getBank(), client)) {
                        model.addAttribute("alreadyExistsMessage", "");
                        setUpView(model, input, new BankInput());
                        return "banks";
                    } else {
                        client = clientDAO.getClientByPassport(client.getPassport());
                        Bank bank = bankDAO.getBankByName(input.getBank());
                        bank.addClient(client);
                        bankDAO.add(bank);

                        return "redirect:/banks/";
                    }
                }
            }

            model.addAttribute("emptyFieldsMessage", "");
            setUpView(model, input, new BankInput());
            return "banks";
        }

        if (!Validator.isValid(input)) {
            setUpView(model, input, new BankInput());
            model.addAttribute("invalidFieldsMessage", "");
            return "banks";
        }

        Client client = new Client(input);
        if (bankDAO.exists(input.getBank(), client)) {
            model.addAttribute("alreadyExistsMessage", "");
            setUpView(model, input, new BankInput());
            return "banks";
        }

        Bank bank = bankDAO.getBankByName(input.getBank());
        bank.addClient(client);
        bankDAO.add(bank);

        if (!clientDAO.existsByPassport(client.getPassport())) {
            clientDAO.add(client);
        }

        return "redirect:/banks/";
    }

    @PostMapping("/addCredit")
    public String addCredit(@ModelAttribute CreditInput input, Model model) {


        return "redirect:/banks/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        bankDAO.delete(id);
        return "redirect:/banks/";
    }

    @GetMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable UUID id) {
        bankDAO.deleteClientById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/deleteCredit/{id}")
    public String deleteCredit(@PathVariable UUID id) {
//        bankDAO.deleteCreditById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/")
    public String showAllBanks(Model model) {
        setUpView(model, new ClientInput(), new BankInput());
        return "banks";
    }
}
