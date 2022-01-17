package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
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
    @Autowired
    private CreditDAO creditDAO;

    private void setUpView(Model model, ClientInput clientInput, CreditInput creditInput, BankInput bankInput) {
        model.addAttribute("banks", bankDAO.findAll());
        model.addAttribute("clientInput", clientInput);
        model.addAttribute("creditInput", creditInput);
        model.addAttribute("bankInput", bankInput);
    }

    private void setUpViewAndAddAttribute(String attribute, Model model,
                                          ClientInput clientInput, CreditInput creditInput, BankInput bankInput) {
        setUpView(model, clientInput, creditInput, bankInput);
        model.addAttribute(attribute);
    }

    @PostMapping("/add")
    public String add(@ModelAttribute BankInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsBankMessage", model, new ClientInput(), new CreditInput(), input);
            return "banks";
        }

        Bank bank = new Bank(input);
        if (bankDAO.existsByName(bank.getName())) {
            setUpViewAndAddAttribute("alreadyExistsBankMessage", model, new ClientInput(), new CreditInput(), input);
            return "banks";
        }

        bankDAO.add(bank);

        return "redirect:/banks/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute BankInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsBankMessage", model, new ClientInput(), new CreditInput(), input);
            return "banks";
        }

        Bank bank = bankDAO.getBankByName(input.getName());

        if (bank == null) {
            setUpViewAndAddAttribute("nonExistentBankMessageEdit", model, new ClientInput(), new CreditInput(), input);
            return "banks";
        }

        if (bankDAO.getBankByName(input.getNewName()) != null) {
            setUpViewAndAddAttribute("alreadyExistsBankNameMessage", model, new ClientInput(), new CreditInput(), input);
            return "banks";
        }

        bank.setName(input.getNewName());
        bankDAO.add(bank);

        return "redirect:/banks/";
    }

    @PostMapping("/addClient")
    public String addClient(@ModelAttribute ClientInput input, Model model) {
        if (input.hasEmptyFields()) {
            if (!input.getBank().isEmpty()) {
                if (!input.getPassport().isEmpty()) {
                    if (bankDAO.existsByName(input.getBank())) {
                        Client client = new Client(input);

                        if (clientDAO.existsByPassport(client.getPassport())) {
                            if (bankDAO.existsClientByBankName(input.getBank(), client)) {
                                setUpViewAndAddAttribute("alreadyExistsClientMessage", model,
                                        input, new CreditInput(), new BankInput());
                                return "banks";
                            } else {
                                client = clientDAO.getClientByPassport(client.getPassport());
                                Bank bank = bankDAO.getBankByName(input.getBank());
                                bank.addClient(client);
                                bankDAO.add(bank);

                                return "redirect:/banks/";
                            }
                        }
                    } else {
                        setUpViewAndAddAttribute("nonExistentBankMessage", model,
                                input, new CreditInput(), new BankInput());
                        return "banks";
                    }
                } else {
                    setUpViewAndAddAttribute("emptyPassportBankMessage", model,
                            input, new CreditInput(), new BankInput());
                    return "banks";
                }
            } else {
                setUpViewAndAddAttribute("emptyFieldsMessage", model,
                        input, new CreditInput(), new BankInput());
                return "banks";
            }
            setUpViewAndAddAttribute("emptyFieldsMessage", model,
                    input, new CreditInput(), new BankInput());
            return "banks";
        }

        if (!Validator.Client.isValid(input)) {
            setUpViewAndAddAttribute("invalidFieldsMessage", model,
                    input, new CreditInput(), new BankInput());
            return "banks";
        }

        Client client = new Client(input);
        if (bankDAO.existsClientByBankName(input.getBank(), client)) {
            setUpViewAndAddAttribute("alreadyExistsClientMessage", model,
                    input, new CreditInput(), new BankInput());
            return "banks";
        }

        if (!clientDAO.existsByPassport(client.getPassport())) {
            clientDAO.add(client);
        }

        Bank bank = bankDAO.getBankByName(input.getBank());
        bank.addClient(client);
        bankDAO.add(bank);

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
        setUpView(model, new ClientInput(), new CreditInput(), new BankInput());
        return "banks";
    }
}
