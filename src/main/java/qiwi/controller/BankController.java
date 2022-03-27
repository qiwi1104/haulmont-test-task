package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.Credit;
import qiwi.model.CreditOffer;
import qiwi.model.input.BankInput;
import qiwi.model.input.ClientInput;
import qiwi.util.StringUtil;
import qiwi.util.Validator;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/banks")
public class BankController {
    @Autowired
    private BankDAO bankDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private CreditDAO creditDAO;

    private void setUpView(Model model, ClientInput clientInput, BankInput bankInput) {
        model.addAttribute("banks", bankDAO.findAll());
        model.addAttribute("clientInput", clientInput);
        model.addAttribute("bankInput", bankInput);
        model.addAttribute("stringUtil", new StringUtil());
    }

    private void setUpViewAndAddAttribute(String attribute, Model model, ClientInput clientInput, BankInput bankInput) {
        setUpView(model, clientInput, bankInput);
        model.addAttribute(attribute, "");
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("bank") @Valid Bank bank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setUpView(model, new ClientInput(), new BankInput());
            return "bank/banks";
        }

        if (bankDAO.existsByName(bank.getName())) {
            setUpView(model, new ClientInput(), new BankInput());
            result.reject("error.alreadyExists", "This bank already exists.");
            return "bank/banks";
        }

        bankDAO.add(bank);

        return "redirect:/banks/";
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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("bank", bankDAO.getBankById(id));

        return "bank/bank-edit";
    }

    @PostMapping("/addClient")
    public String addClient(@ModelAttribute ClientInput input, Model model) {
        boolean hasErrors = false;

        if (!input.getPassport().isEmpty()) {
            if (!Validator.Bank.isPassportValid(input)) {
                setUpViewAndAddAttribute("invalidPassportBankMessage", model, input, new BankInput());
                hasErrors = true;
            }
        }

        if (input.isEmptyPassportOrBank()) {
            setUpViewAndAddAttribute("emptyFieldsAddClientBankMessage", model, input, new BankInput());
            hasErrors = true;
        } else {
            if (hasErrors) {
                return "bank/banks";
            }

            if (!clientDAO.existsByPassport(input.getPassport())) {
                setUpViewAndAddAttribute("nonExistentClientMessage", model, input, new BankInput());
            }

            if (bankDAO.existsByName(input.getBank())) {
                Client client = new Client(input);

                if (clientDAO.existsByPassport(client.getPassport())) {
                    if (bankDAO.existsClientByBankName(input.getBank(), client)) {
                        setUpViewAndAddAttribute("alreadyExistsClientMessage", model, input, new BankInput());
                        hasErrors = true;
                    } else {
                        client = clientDAO.getClientByPassport(client.getPassport());
                        Bank bank = bankDAO.getBankByName(input.getBank());
                        bank.addClient(client);
                        bankDAO.add(bank);

                        return "redirect:/banks/";
                    }
                } else {
                    setUpViewAndAddAttribute("nonExistentClientMessage", model, input, new BankInput());
                    hasErrors = true;
                }
            } else {
                setUpViewAndAddAttribute("nonExistentBankMessage", model, input, new BankInput());
                hasErrors = true;
            }
        }

        if (hasErrors) {
            return "bank/banks";
        }

        Client client = clientDAO.getClientByPassport(input.getPassport());
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
        setUpView(model, new ClientInput(), new BankInput());

        model.addAttribute("bank", new Bank());
        return "bank/banks";
    }
}
