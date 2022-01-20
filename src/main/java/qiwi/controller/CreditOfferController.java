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

        BigDecimal interest = BigDecimal
                .valueOf(Double.parseDouble(input.getInterest()))
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal sum = BigDecimal
                .valueOf(Double.parseDouble(input.getSum()))
                .setScale(5, RoundingMode.HALF_UP);

        BigDecimal monthlyInterest = interest
                .divide(BigDecimal.valueOf(100 * 12), RoundingMode.HALF_UP)
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal temp = monthlyInterest
                .divide(BigDecimal.ONE
                        .subtract(monthlyInterest
                                .add(BigDecimal.ONE)
                                .pow(-months, new MathContext(10))), RoundingMode.HALF_UP)
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal monthlyPaymentSum = sum
                .multiply(temp)
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal remainsCreditSum = sum;

        creditOffer.setSum(sum);

        for (int i = 0; i < months; i++) {
            BigDecimal interestSum = remainsCreditSum
                    .multiply(monthlyInterest)
                    .setScale(5, RoundingMode.HALF_UP);
            BigDecimal creditSum = monthlyPaymentSum
                    .subtract(interestSum)
                    .setScale(5, RoundingMode.HALF_UP);

            Payment payment = new Payment(date, monthlyPaymentSum, creditSum, interestSum);
            creditOffer.addPayment(payment);
            payment.setCreditOffer(creditOffer);

            date = date.plusMonths(1);
            remainsCreditSum = remainsCreditSum
                    .subtract(creditSum)
                    .setScale(5, RoundingMode.HALF_UP);
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("creditOfferInput") CreditOfferInput input, Model model) {
        String bank = input.getBank();
        String passport = input.getPassport();
        boolean hasErrors = false;

        if (input.hasEmptyFields()) {
            setUpViewAndAddAttribute("emptyFieldsCreditOfferMessage", model, input);
            return "creditOffers";
        }

        if (!Validator.CreditOffer.isValid(input)) {
            setUpViewAndAddAttribute("invalidFieldsCreditOfferMessage", model, input);
            hasErrors = true;
        }

        if (!Validator.CreditOffer.isValidCreditDetail(input)) {
            setUpViewAndAddAttribute("invalidCreditDetailsMessage", model, input);
            hasErrors = true;
        }

        if (hasErrors) {
            return "creditOffers";
        }

        BigDecimal months = BigDecimal.valueOf(Long.parseLong(input.getMonths()));
        BigDecimal limit = BigDecimal
                .valueOf(Double.parseDouble(input.getLimit()))
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal interest = BigDecimal.valueOf(Long.parseLong(input.getInterest()));
        BigDecimal sum = BigDecimal
                .valueOf(Double.parseDouble(input.getSum()))
                .setScale(5, RoundingMode.HALF_UP);

        if (!bankDAO.existsByName(bank)) {
            setUpViewAndAddAttribute("nonExistentBankInCreditOfferMessage", model, input);
            hasErrors = true;
        }

        if (creditDAO.getCredit(bank, limit, interest) == null) {
            setUpViewAndAddAttribute("nonExistentCreditMessage", model, input);
            hasErrors = true;
        }

        if (Validator.CreditOffer.isValid(input)) {
            if (bankDAO.existsByName(bank)) {
                if (!bankDAO.existsClientByPassport(bank, passport)) {
                    setUpViewAndAddAttribute("nonExistentClientInCreditOfferMessage", model, input);
                    hasErrors = true;
                }
            } else {
                if (!clientDAO.existsByPassport(passport)) {
                    setUpViewAndAddAttribute("nonExistentClientInCreditOfferMessage", model, input);
                    hasErrors = true;
                }
            }
        }

        if (hasErrors) {
            return "creditOffers";
        }

        if (months.compareTo(BigDecimal.ZERO) != 1) {
            setUpViewAndAddAttribute("monthsErrorMessage", model, input);
            hasErrors = true;
        }

        if (limit.compareTo(BigDecimal.ZERO) != 1) {
            setUpViewAndAddAttribute("limitErrorMessage", model, input);
            hasErrors = true;
        }

        if (sum.compareTo(BigDecimal.ZERO) != 1 || sum.compareTo(limit) == 1) {
            setUpViewAndAddAttribute("sumErrorMessage", model, input);
            hasErrors = true;
        }

        if (hasErrors) {
            return "creditOffers";
        }

        CreditOffer creditOffer = new CreditOffer();
        calculatePayments(input, creditOffer);

        creditOffer.setClient(clientDAO.getClientByPassport(passport));
        creditOffer.setCredit(creditDAO.getCredit(bank, limit, interest));
        creditOffer.setBank(bankDAO.getBankByName(bank));

        if (creditOfferDAO.exists(creditOffer)) {
            setUpViewAndAddAttribute("alreadyExistsCreditOfferMessage", model, input);
            return "creditOffers";
        }

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
