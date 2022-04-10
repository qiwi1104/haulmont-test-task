package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.dao.impl.CreditOfferDAO;
import qiwi.util.StringUtil;

import java.util.UUID;

@Controller
@RequestMapping("/credit-offers")
public class CreditOfferController {
    @Autowired
    private CreditOfferDAO creditOfferDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private CreditDAO creditDAO;
    @Autowired
    private BankDAO bankDAO;

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        creditOfferDAO.deleteById(id);
        return "redirect:/credit-offers/";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        model.addAttribute("creditOffers", creditOfferDAO.findAll());
        model.addAttribute("stringUtil", new StringUtil());

        return "credit offer/creditOffers";
    }
}
