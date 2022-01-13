package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.model.Client;
import qiwi.model.input.ClientInput;
import qiwi.util.Validator;

import java.util.UUID;

@Controller
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private BankDAO bankDAO;
    @Autowired
    private CreditDAO creditDAO;

    private void setUpView(Model model, ClientInput input) {
        model.addAttribute("clients", clientDAO.findAll());
        model.addAttribute("clientInput", input);
    }

    private boolean updateClient(Client client, ClientInput input) {
        if (input.getFirstName() != null && !input.getFirstName().equals("")) {
            client.setFirstName(input.getFirstName());
        }
        if (input.getMiddleName() != null && !input.getMiddleName().equals("")) {
            client.setMiddleName(input.getMiddleName());
        }
        if (input.getLastName() != null && !input.getLastName().equals("")) {
            client.setLastName(input.getLastName());
        }
        if (input.getPhone() != null && !input.getPhone().equals("")) {
            client.setPhone(input.getPhone());
        }
        if (input.getMail() != null && !input.getMail().equals("")) {
            client.setMail(input.getMail());
        }
        if (input.getNewPassport() != null && !input.getNewPassport().equals("")) {
            if (!clientDAO.existsByPassport(input.getNewPassport())) {
                client.setPassport(input.getNewPassport());
            } else {
                return false;
            }
        }

        return true;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("clientInput") ClientInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpView(model, input);
            model.addAttribute("emptyFieldsMessage", "");
            return "clients";
        }

        if (!Validator.Client.isValid(input)) {
            setUpView(model, input);
            model.addAttribute("invalidFieldsMessage", "");
            return "clients";
        }

        Client client = new Client(input);
        if (clientDAO.exists(client)) {
            setUpView(model, input);
            model.addAttribute("alreadyExistsMessage", "");
            return "clients";
        }

        clientDAO.add(client);

        return "redirect:/clients/";
    }

    @PostMapping("/edit/{passport}")
    public String edit(@ModelAttribute("clientInput") ClientInput input, Model model) {
        if (input.getPassport().isEmpty()) {
            setUpView(model, input);
            model.addAttribute("emptyPassportMessage", "");
            return "clients";
        }

        if (!Validator.Client.isValidEdit(input)) {
            setUpView(model, input);
            model.addAttribute("invalidFieldsMessage", "");
            return "clients";
        }

        Client client = clientDAO.getClientByPassport(input.getPassport());
        if (updateClient(client, input)) {
            clientDAO.add(client);
        } else {
            setUpView(model, input);
            model.addAttribute("alreadyExists", "");
            return "clients";
        }

        return "redirect:/clients/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, Model model) {
        if (clientDAO.hasCredit(id)) {
            setUpView(model, new ClientInput());
            model.addAttribute("hasCreditMessage", "");
            return "clients";
        }

        clientDAO.delete(id);
        return "redirect:/clients/";
    }

    @GetMapping("/deleteCredit/{id}")
    public String deleteCredit(@PathVariable UUID id) {
        creditDAO.delete(id);
        return "redirect:/clients/";
    }

    @GetMapping("/")
    public String showAllClients(Model model) {
        setUpView(model, new ClientInput());
        return "clients";
    }
}
