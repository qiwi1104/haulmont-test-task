package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.model.Client;
import qiwi.service.ClientService;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/add")
    public String add(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {
        return clientService.add(client, result, model)
                ? "redirect:/clients/"
                : "client/clients";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));

        return "client/client-edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {
        return clientService.edit(client, result, model)
                ? "redirect:/clients/"
                : "client/client-edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        clientService.deleteById(id);
        return "redirect:/clients/";
    }

    @GetMapping("/")
    public String showAllClients(Model model) {
        clientService.showAllClients(model);

        return "client/clients";
    }
}
