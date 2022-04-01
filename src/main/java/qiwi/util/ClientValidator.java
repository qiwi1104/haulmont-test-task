package qiwi.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import qiwi.model.Client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Client.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Client client = (Client) o;

        Pattern pattern = Pattern.compile("[a-zA-Z]+");

        Matcher matcher = pattern.matcher(client.getFirstName());
        if (client.getFirstName().isEmpty()) {
            errors.reject("firstName", "First Name must not be empty.");
        } else {
            if (!matcher.matches()) {
                errors.reject("firstName", "First Name field has illegal characters.");
            }
        }

        matcher = pattern.matcher(client.getMiddleName());
        if (!client.getMiddleName().isEmpty()) {
            if (!matcher.matches()) {
                errors.reject("middleName", "Middle Name field has illegal characters.");
            }
        }

        if (client.getLastName().isEmpty()) {
            errors.reject("lastName", "Last Name must not be empty.");
        } else {
            matcher = pattern.matcher(client.getLastName());
            if (!matcher.matches()) {
                errors.reject("lastName", "Last Name field has illegal characters.");
            }
        }

        pattern = Pattern.compile("^\\d{11}|^\\+[7]\\d{10}");
        if (client.getPhone().isEmpty()) {
            errors.reject("phone", "Phone must not be empty.");
        } else {
            matcher = pattern.matcher(client.getPhone());
            if (!matcher.matches()) {
                errors.reject("phone", "Phone field has illegal characters.");
            }
        }

        pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[a-zA-Z]{2,6}", Pattern.CASE_INSENSITIVE);
        if (client.getMail().isEmpty()) {
            errors.reject("mail", "Mail must not be empty.");
        } else {
            matcher = pattern.matcher(client.getMail());
            if (!matcher.matches()) {
                errors.reject("mail", "Invalid mail address.");
            }
        }

        pattern = Pattern.compile("^\\d{10}");
        matcher = pattern.matcher(client.getPassport());
        if (!matcher.matches()) {
            errors.reject("passport", "Passport field has to be 10 digits long.");
        }
    }
}
