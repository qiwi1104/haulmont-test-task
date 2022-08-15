package qiwi.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import qiwi.model.Credit;
import qiwi.model.CreditOffer;

import java.math.BigDecimal;

public class CreditOfferValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CreditOffer.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreditOffer creditOffer = (CreditOffer) o;
        Credit credit = creditOffer.getCredit();

        boolean creditIsNull = credit == null;
        boolean sumIsNull = creditOffer.getSum() == null;

        if (creditIsNull) {
            errors.reject("credit", "Please, select credit.");
        }

        if (sumIsNull) {
            errors.reject("sumError", "Sum field has illegal characters.");
        }

        if (!sumIsNull && !creditIsNull) {
            if (creditOffer.getSum().compareTo(BigDecimal.ZERO) != 1
                    || creditOffer.getSum().compareTo(credit.getLimit()) == 1) {
                errors.reject("sumError", "Sum must be between 0 and credit limit.");
            }
        }
    }

    public void validate(Object o, Errors errors, Integer months) {
        validate(o, errors);

        if (months == null) {
            errors.reject("monthsError", "Months field must not be empty.");
        } else {
            try {
                if (months <= 0) {
                    errors.reject("monthsError", "Amount of months must be above 0.");
                }
            } catch (NumberFormatException e) {
                errors.reject("monthsError", "Months field has illegal characters.");
            }
        }
    }
}
