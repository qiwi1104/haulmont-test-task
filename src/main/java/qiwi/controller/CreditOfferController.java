package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.*;
import qiwi.model.CreditOffer;
import qiwi.model.Payment;
import qiwi.model.input.CreditOfferInput;
import qiwi.util.Validator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
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
    @Autowired
    private PaymentDAO paymentDAO;

    private void setUpView(Model model, CreditOfferInput input) {
        model.addAttribute("creditOfferInput", input);
        model.addAttribute("creditOffers", creditOfferDAO.findAll());
    }

    private BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditOfferInput") CreditOfferInput input, Model model) {
        if (input.hasEmptyFields()) {
            setUpView(model, input);
            model.addAttribute("", "");
            return "creditOffers";
        }

        if (!Validator.CreditOffer.isValid(input)) {
            setUpView(model, input);
            model.addAttribute("", "");
            return "creditOffers";
        }

        CreditOffer creditOffer = new CreditOffer();

        LocalDate date = LocalDate.now();
        int months = Integer.parseInt(input.getMonths());

        BigDecimal interest = BigDecimal
                .valueOf(Double.parseDouble(input.getInterest()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal sum = BigDecimal
                .valueOf(Double.parseDouble(input.getSum()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal monthlyInterest = interest.divide(BigDecimal.valueOf(100 * 12), RoundingMode.HALF_UP);
        BigDecimal monthlyPaymentSum = sum
                .multiply(monthlyInterest
                        .divide(BigDecimal.ONE
                                .subtract(monthlyInterest
                                        .add(BigDecimal.ONE)
                                        .pow(-months, new MathContext(2))), RoundingMode.HALF_UP));
        BigDecimal remains = sum;

        creditOffer.setSum(sum);

        for (int i = 1; i < months; i++) {
            BigDecimal interestSum = remains.multiply(monthlyInterest);
            BigDecimal creditSum = monthlyPaymentSum.subtract(interestSum);

            Payment payment = new Payment(date, monthlyPaymentSum, creditSum, interestSum);
            creditOffer.addPayment(payment);
            payment.setCreditOffer(creditOffer);

            date = date.plusMonths(1);
            remains = remains.subtract(monthlyPaymentSum);
        }

        creditOffer.setClient(clientDAO.getClientByPassport(input.getPassport()));
        creditOffer.setCredit(creditDAO.getCredit(input.getBank(),
                BigDecimal.valueOf(Double.parseDouble(input.getLimit())),
                BigDecimal.valueOf(Double.parseDouble(input.getInterest()))));
        creditOffer.setBank(bankDAO.getBankByName(input.getBank()));

        clientDAO.addCreditOffer(input.getPassport(), creditOffer);
        creditOfferDAO.add(creditOffer);
        creditDAO.getCredit(input.getBank(),
                BigDecimal.valueOf(Double.parseDouble(input.getLimit())),
                BigDecimal.valueOf(Double.parseDouble(input.getInterest()))).addCreditOffer(creditOffer);

        return "redirect:/credit-offers/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        creditOfferDAO.deleteById(id);
        return "redirect:/credit-offers/";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        setUpView(model, new CreditOfferInput());
        return "creditOffers";
    }
}
