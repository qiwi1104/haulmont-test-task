package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import qiwi.service.CreditOfferService;
import qiwi.util.StringUtil;

import java.util.UUID;

@Controller
@RequestMapping("/credit-offers")
public class CreditOfferController {
    @Autowired
    private CreditOfferService creditOfferService;

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        creditOfferService.deleteById(id);
        return "redirect:/credit-offers/";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        model.addAttribute("creditOffers", creditOfferService.findAll());
        model.addAttribute("stringUtil", new StringUtil());

        return "credit offer/creditOffers";
    }
}
