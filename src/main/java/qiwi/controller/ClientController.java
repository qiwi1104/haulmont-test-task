package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.ClientDAO;
import qiwi.model.Client;
import qiwi.model.input.ClientInput;
import qiwi.util.Validator;

import java.util.UUID;

@Controller
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientDAO clientDAO;

    private void setUpView(Model model) {
        model.addAttribute("clients", clientDAO.findAll());
    }

    private void updateClient(Client client, ClientInput input) {
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
        if (input.getPassport() != null && !input.getPassport().equals("")) {
            client.setPassport(input.getPassport());
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("clientInput") ClientInput input, Model model) {
        if (input.hasEmptyFields()) {
            model.addAttribute("emptyFieldsMessage", "");
            setUpView(model);
            return "clients";
        }

        if (!Validator.isValid(input)) {
            setUpView(model);
            model.addAttribute("invalidFieldsMessage", "");
            return "clients";
        }

        Client client = new Client(input);
        if (clientDAO.exists(client)) {
            model.addAttribute("alreadyExistsMessage", "");
            setUpView(model);
            return "clients";
        }

        clientDAO.add(client);

        return "redirect:/clients/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("clientInput") ClientInput input, BindingResult result, Model model) {
        if (!Validator.isValid(input)) {
            setUpView(model);
            model.addAttribute("invalidFieldsMessage", "");
            return "clients";
        }

        Client client = clientDAO.getClientById(input.getId());
        updateClient(client, input);
        clientDAO.add(client);

        return "redirect:/clients/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        clientDAO.delete(id);
        return "redirect:/clients/";
    }

    @GetMapping("/")
    public String showAllClients(Model model) {
        setUpView(model);
        return "clients";
    }
}
