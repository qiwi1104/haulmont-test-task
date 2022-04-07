package qiwi.util;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import qiwi.model.Credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Credit.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Credit credit = (Credit) o;

        if (credit.getLimit() == null) {
            errors.reject("limit", "Limit field has illegal characters.");
        } else {
            if (credit.getLimit().compareTo(BigDecimal.ZERO) != 1) {
                errors.reject("limit", "Limit must be above zero.");
            }
        }

        if (credit.getInterest() == null) {
            errors.reject("interest", "Interest field has illegal characters.");
        } else {
            if (credit.getInterest().compareTo(BigDecimal.ZERO) == -1) {
                errors.reject("interest", "Interest must not be negative.");
            }
        }
    }
}
