package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private CreditDAO creditDAO;

    private void setUpView(Model model, ClientInput input) {
        model.addAttribute("clients", clientDAO.findAll());
        model.addAttribute("clientInput", input);
    }

    private void setUpViewAndAddAttribute(String attribute, Model model, ClientInput input) {
        setUpView(model, input);
        model.addAttribute(attribute, "");
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
        if (input.getNewPassport() != null && !input.getNewPassport().equals("")) {
            client.setPassport(input.getNewPassport());
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("clientInput") ClientInput input, Model model) {
        boolean hasErrors = false;

        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsMessage", model, input);
            return "clients";
        }

        if (!Validator.Client.isValid(input)) {
            setUpViewAndAddAttribute("invalidFieldsMessage", model, input);
            hasErrors = true;
        }

        if (clientDAO.existsByPassport(input.getPassport())) {
            setUpViewAndAddAttribute("alreadyExistsMessage", model, input);
            hasErrors = true;
        }

        for (Client client : clientDAO.findAll()) {
            if (client.getPhone().equals(input.getPhone())
                    || client.getMail().equals(input.getMail())
                    || client.getPassport().equals(input.getPassport())) {

                setUpView(model, input);

                if (client.getPhone().equals(input.getPhone())) {
                    model.addAttribute("occupiedPhoneMessage", "");
                }

                if (client.getMail().equals(input.getMail())) {
                    model.addAttribute("occupiedMailMessage", "");
                }

                hasErrors = true;
            }
        }

        if (hasErrors) {
            return "clients";
        }

        clientDAO.add(new Client(input));

        return "redirect:/clients/";
    }

    @PostMapping("/edit/{passport}")
    public String edit(@ModelAttribute("clientInput") ClientInput input, Model model) {
        boolean hasErrors = false;

        if (input.getPassport().isEmpty()) {
            setUpViewAndAddAttribute("emptyPassportMessage", model, input);
            hasErrors = true;
        }

        if (!Validator.Client.isValidEdit(input)) {
            setUpViewAndAddAttribute("invalidFieldsMessageEdit", model, input);
            hasErrors = true;
        }

        for (Client client : clientDAO.findAll()) {
            if (client.getPhone().equals(input.getPhone())
                    || client.getMail().equals(input.getMail())) {

                setUpView(model, input);

                if (client.getPhone().equals(input.getPhone())) {
                    model.addAttribute("occupiedPhoneMessageEdit", "");
                }

                if (client.getMail().equals(input.getMail())) {
                    model.addAttribute("occupiedMailMessageEdit", "");
                }

                hasErrors = true;
            }
        }

        if (!input.getNewPassport().isEmpty()) {
            if (clientDAO.existsByPassport(input.getNewPassport())) {
                setUpViewAndAddAttribute("alreadyExistsMessageEdit", model, input);
                hasErrors = true;
            }
        }

        if (!clientDAO.existsByPassport(input.getPassport())) {
            setUpViewAndAddAttribute("nonExistentMessageEdit", model, input);
            hasErrors = true;
        }

        if (hasErrors) {
            return "clients";
        }

        Client client = clientDAO.getClientByPassport(input.getPassport());
        updateClient(client, input);
        clientDAO.add(client);

        return "redirect:/clients/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        clientDAO.deleteById(id);
        return "redirect:/clients/";
    }

    @GetMapping("/")
    public String showAllClients(Model model) {
        setUpView(model, new ClientInput());
        return "clients";
    }
}
