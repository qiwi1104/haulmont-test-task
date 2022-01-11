package qiwi.model.input;

import javax.validation.constraints.Email;

public class ClientInput {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    @Email
    private String mail;
    private String passport;

    public boolean hasEmptyFields() {
        return firstName.equals("") || lastName.equals("")
                || phone.equals("") || mail.equals("") || passport.equals("");
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getPassport() {
        return passport;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
