package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.Credit;
import qiwi.model.CreditOffer;
import qiwi.service.BankService;
import qiwi.service.CreditService;
import qiwi.util.StringUtil;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@SessionAttributes(types = {UUID.class, String.class},
        names = {"bankId", "passport"})
@RequestMapping("/banks")
public class BankController {
    @Autowired
    private BankService bankService;
    @Autowired
    private CreditService creditService;

    @PostMapping("/add")
    public String add(@ModelAttribute("bank") @Valid Bank bank, BindingResult result, Model model) {
        return bankService.add(bank, result, model)
                ? "redirect:/banks/"
                : "bank/banks";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("bank", bankService.getBankById(id));

        return "bank/bank-edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("bank") @Valid Bank bank, BindingResult result) {
        return bankService.edit(bank, result)
                ? "redirect:/banks/"
                : "bank/bank-edit";
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
        return bankService.addClient(client, result, id, status)
                ? "redirect:/banks/"
                : "bank/add-client";
    }

    @GetMapping("/add-credit-offer/{id}/{passport}")
    public String addCreditOffer(@PathVariable UUID id, @PathVariable String passport, Model model) {
        model.addAttribute("bankId", id);
        model.addAttribute("passport", passport);
        model.addAttribute("months", 0);

        model.addAttribute("bank", bankService.getBankById(id));
        model.addAttribute("credit", new Credit());
        model.addAttribute("creditOffer", new CreditOffer());
        model.addAttribute("stringUtil", new StringUtil());

        return "bank/add-credit-offer";
    }

    @PostMapping("/add-credit-offer")
    public String addCreditOffer(@ModelAttribute("creditOffer") @Valid CreditOffer creditOffer, BindingResult result,
                                 @ModelAttribute("months") Integer months, Model model) {

        return bankService.addCreditOffer(creditOffer, result, months, model)
                ? "redirect:/banks/"
                : "bank/add-credit-offer";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        bankService.deleteById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/delete-client/{bankId}/{id}")
    public String deleteClient(@PathVariable UUID bankId, @PathVariable UUID id) {
        bankService.deleteClientById(bankId, id);
        return "redirect:/banks/";
    }

    @GetMapping("/delete-credit/{id}")
    public String deleteCredit(@PathVariable UUID id) {
        creditService.deleteById(id);
        return "redirect:/banks/";
    }

    @GetMapping("/")
    public String showAllBanks(Model model) {
        bankService.showAllBanks(model);
        return "bank/banks";
    }
}
