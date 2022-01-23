package qiwi.util;

import qiwi.model.input.ClientInput;
import qiwi.model.input.CreditEditInput;
import qiwi.model.input.CreditInput;
import qiwi.model.input.CreditOfferInput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static class Client {

        public static boolean isValidEdit(ClientInput input) {
            Pattern pattern = Pattern.compile("[a-zA-Z]+");

            Matcher matcher = pattern.matcher(input.getFirstName());
            if (!input.getFirstName().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            matcher = pattern.matcher(input.getMiddleName());
            if (!input.getMiddleName().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            matcher = pattern.matcher(input.getLastName());
            if (!input.getLastName().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            pattern = Pattern.compile("^\\d{11}|^\\+[7]\\d{10}");
            matcher = pattern.matcher(input.getPhone());
            if (!input.getPhone().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[a-zA-Z]{2,6}", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(input.getMail());
            if (!input.getMail().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            pattern = Pattern.compile("^\\d{10}");
            matcher = pattern.matcher(input.getNewPassport());
            if (!input.getNewPassport().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            if (!input.getPassport().isEmpty()) {
                matcher = pattern.matcher(input.getPassport());
                return matcher.matches();
            }

            return true;
        }

        public static boolean isValid(ClientInput input) {
            Pattern pattern = Pattern.compile("[a-zA-Z]+");

            Matcher matcher = pattern.matcher(input.getFirstName());
            if (!matcher.matches()) {
                return false;
            }

            matcher = pattern.matcher(input.getMiddleName());
            if (!input.getMiddleName().isEmpty()) {
                if (!matcher.matches()) {
                    return false;
                }
            }

            matcher = pattern.matcher(input.getLastName());
            if (!matcher.matches()) {
                return false;
            }

            pattern = Pattern.compile("^\\d{11}|^\\+[7]\\d{10}");
            matcher = pattern.matcher(input.getPhone());
            if (!matcher.matches()) {
                return false;
            }

            pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[a-zA-Z]{2,6}", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(input.getMail());
            if (!matcher.matches()) {
                return false;
            }

            pattern = Pattern.compile("^\\d{10}");
            matcher = pattern.matcher(input.getPassport());
            return matcher.matches();
        }
    }

    public static class Bank {
        public static boolean isPassportValid(ClientInput input) {
            Pattern pattern = Pattern.compile("^\\d{10}");
            Matcher matcher = pattern.matcher(input.getPassport());
            return matcher.matches();
        }
    }

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
