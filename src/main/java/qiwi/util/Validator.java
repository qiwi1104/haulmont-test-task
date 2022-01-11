package qiwi.util;

import qiwi.model.input.ClientInput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
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
