package qiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.dao.impl.BankDAO;
import qiwi.dao.impl.ClientDAO;
import qiwi.dao.impl.CreditDAO;
import qiwi.dao.impl.CreditOfferDAO;
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

    private void setUpView(Model model, CreditOfferInput input) {
        model.addAttribute("creditOfferInput", input);
        model.addAttribute("creditOffers", creditOfferDAO.findAll());
    }

    private void setUpViewAndAddAttribute(String attribute, Model model, CreditOfferInput input) {
        setUpView(model, input);
        model.addAttribute(attribute, "");
    }

    private void calculatePayments(CreditOfferInput input, CreditOffer creditOffer) {
        LocalDate date = LocalDate.now();
        int months = Integer.parseInt(input.getMonths());

        BigDecimal interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));
        BigDecimal sum = BigDecimal.valueOf(Double.parseDouble(input.getSum()));

        BigDecimal monthlyInterest = interest.divide(BigDecimal.valueOf(100 * 12), RoundingMode.HALF_UP);
        BigDecimal temp = monthlyInterest
                .divide(BigDecimal.ONE
                        .subtract(monthlyInterest
                                .add(BigDecimal.ONE)
                                .pow(-months, new MathContext(10))), RoundingMode.HALF_UP);
        BigDecimal monthlyPaymentSum = sum.multiply(temp);
        BigDecimal remainsCreditSum = sum;

        creditOffer.setSum(sum);

        for (int i = 1; i <= months; i++) {
            BigDecimal interestSum = remainsCreditSum.multiply(monthlyInterest);
            BigDecimal creditSum = monthlyPaymentSum.subtract(interestSum);

            Payment payment = new Payment(date, monthlyPaymentSum, creditSum, interestSum);
            creditOffer.addPayment(payment);
            payment.setCreditOffer(creditOffer);

            date = date.plusMonths(1);
            remainsCreditSum = remainsCreditSum.subtract(creditSum);
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditOfferInput") CreditOfferInput input, Model model) {
        String bank = input.getBank();
        String passport = input.getPassport();

        if (input.hasEmptyFields()) {
            setUpView(model, input);
            model.addAttribute("emptyFieldsCreditOfferMessage", "");
            return "creditOffers";
        }

        BigDecimal months = BigDecimal.valueOf(Long.parseLong(input.getMonths()));
        BigDecimal limit = BigDecimal.valueOf(Long.parseLong(input.getLimit()));
        BigDecimal interest = BigDecimal.valueOf(Long.parseLong(input.getInterest()));
        BigDecimal sum = BigDecimal.valueOf(Long.parseLong(input.getSum()));

        if (!Validator.CreditOffer.isValid(input)) {
            setUpView(model, input);
            model.addAttribute("invalidFieldsCreditOfferMessage", "");
            return "creditOffers";
        }

        if (!Validator.CreditOffer.isValidCreditDetail(input)) {
            setUpView(model, input);
            model.addAttribute("invalidCreditDetailsMessage", "");
            return "creditOffers";
        }

        if (!bankDAO.existsByName(bank)) {
            setUpView(model, input);
            model.addAttribute("nonExistentBankInCreditOfferMessage", "");
            return "creditOffers";
        }

        if (!bankDAO.existsClientByPassport(bank, passport)) {
            setUpView(model, input);
            model.addAttribute("nonExistentClientInCreditOfferMessage", "");
            return "creditOffers";
        }

        if (months.compareTo(BigDecimal.ZERO) != 1) {
            setUpView(model, input);
            model.addAttribute("monthsErrorMessage", "");
            return "creditOffers";
        }

        if (limit.compareTo(BigDecimal.ZERO) != 1) {
            setUpView(model, input);
            model.addAttribute("limitErrorMessage", "");
            return "creditOffers";
        }

        if (sum.compareTo(BigDecimal.ZERO) != 1 || sum.compareTo(limit) == 1) {
            setUpView(model, input);
            model.addAttribute("sumErrorMessage", "");
            return "creditOffers";
        }

        if (creditDAO.getCredit(bank, limit, interest) == null) {
            setUpView(model, input);
            model.addAttribute("nonExistentCreditMessage", "");
            return "creditOffers";
        }

        CreditOffer creditOffer = new CreditOffer();
        calculatePayments(input, creditOffer);

        creditOffer.setClient(clientDAO.getClientByPassport(passport));
        creditOffer.setCredit(creditDAO.getCredit(bank, limit, interest));
        creditOffer.setBank(bankDAO.getBankByName(bank));

        clientDAO.addCreditOffer(passport, creditOffer);
        creditOfferDAO.add(creditOffer);

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
