package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.model.Client;
import qiwi.util.ClientValidator;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private CreditDAO creditDAO;

    @PostMapping("/add")
    public String add(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {
        ClientValidator validator = new ClientValidator();
        validator.validate(client, result);

        if (result.hasErrors()) {
            model.addAttribute("clients", clientDAO.findAll());
            return "client/clients";
        }

        if (clientDAO.existsByPassport(client.getPassport())) {
            model.addAttribute("clients", clientDAO.findAll());
            result.reject("alreadyExists", "This person already exists.");
            return "client/clients";
        }

        for (Client c : clientDAO.findAll()) {
            if (c.equalsPhone(client.getPhone()) || c.getMail().equals(client.getMail())) {

                if (c.equalsPhone(client.getPhone())) {
                    result.reject("phoneOccupied", "This phone is occupied.");
                }

                if (c.getMail().equals(client.getMail())) {
                    result.reject("mailOccupied", "This mail is occupied.");
                }

                model.addAttribute("clients", clientDAO.findAll());
                return "client/clients";
            }
        }

        clientDAO.add(client);

        return "redirect:/clients/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("client", clientDAO.getClientById(id));

        return "client/client-edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {
        ClientValidator validator = new ClientValidator();
        validator.validate(client, result);

        if (result.hasErrors()) {
            model.addAttribute("clients", clientDAO.findAll());
            return "client/client-edit";
        }

        if (clientDAO.existsByPassport(client.getPassport())) {
            if (clientDAO.getClientByPassport(client.getPassport()).getId().compareTo(client.getId()) != 0) {
                model.addAttribute("clients", clientDAO.findAll());
                result.reject("alreadyExists", "This person already exists.");
                return "client/client-edit";
            }
        }

        for (Client c : clientDAO.findAll()) {
            if (c.getId().compareTo(client.getId()) != 0) {
                if (c.equalsPhone(client.getPhone()) || c.getMail().equals(client.getMail())) {
                    if (c.equalsPhone(client.getPhone())) {
                        result.reject("phoneOccupied", "This phone is occupied.");
                    }

                    if (c.getMail().equals(client.getMail())) {
                        result.reject("mailOccupied", "This mail is occupied.");
                    }

                    model.addAttribute("clients", clientDAO.findAll());
                    return "client/client-edit";
                }
            }
        }

        clientDAO.update(clientDAO.getClientById(client.getId()), client);

        return "redirect:/clients/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        clientDAO.deleteById(id);
        return "redirect:/clients/";
    }

    @GetMapping("/")
    public String showAllClients(Model model) {
        model.addAttribute("clients", clientDAO.findAll());
        model.addAttribute("client", new Client());

        return "client/clients";
    }
}
