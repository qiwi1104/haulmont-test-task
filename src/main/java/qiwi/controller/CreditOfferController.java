package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import qiwi.dao.impl.CreditOfferDAO;
import qiwi.model.input.CreditOfferInput;

@Controller
@RequestMapping("/credit-offers")
public class CreditOfferController {
    @Autowired
    private CreditOfferDAO creditOfferDAO;

    private void setUpView(Model model, CreditOfferInput input) {
        model.addAttribute("creditOfferInput", input);
        model.addAttribute("creditOffers", creditOfferDAO.findAll());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditOfferInput") CreditOfferInput input, Model model) {

        return "creditOffers";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        setUpView(model, new CreditOfferInput());
        return "creditOffers";
    }
}
