package qiwi.util;

import qiwi.model.input.CreditInput;
import qiwi.model.input.CreditOfferInput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static class Credit {

        public static boolean isValid(CreditInput input) {
            Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)|[0-9]+");

            Matcher matcher = pattern.matcher(input.getLimit());
            if (!matcher.matches()) {
                return false;
            }

            matcher = pattern.matcher(input.getInterest());
            return matcher.matches();
        }
    }

    public static class CreditOffer {
        public static boolean isValid(CreditOfferInput input) {
            Pattern pattern = Pattern.compile("[0-9]+");

            Matcher matcher = pattern.matcher(input.getMonths());
            if (!matcher.matches()) {
                return false;
            }

            pattern = Pattern.compile("[0-9]+(\\.[0-9]+)|[0-9]+");
            matcher = pattern.matcher(input.getSum());
            if (!matcher.matches()) {
                return false;
            }

            matcher = pattern.matcher(input.getLimit());
            if (!matcher.matches()) {
                return false;
            }

            matcher = pattern.matcher(input.getInterest());
            if (!matcher.matches()) {
                return false;
            }

            pattern = Pattern.compile("^\\d{10}");
            matcher = pattern.matcher(input.getPassport());
            return matcher.matches();
        }

        public static boolean isValidCreditDetail(CreditOfferInput input) {
            CreditInput creditInput = new CreditInput();
            creditInput.setBank(input.getBank());
            creditInput.setLimit(input.getLimit());
            creditInput.setInterest(input.getInterest());

            return Validator.Credit.isValid(creditInput);
        }
    }
}
