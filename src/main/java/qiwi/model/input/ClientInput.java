package qiwi.model.input;

public class ClientInput {
    private String passport = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String phone = "";
    private String mail = "";
    private String newPassport = "";
    private String bank = "";

    public boolean hasEmptyFields() {
        return firstName == null || firstName.isEmpty()
                || lastName == null || lastName.isEmpty()
                || phone == null || phone.isEmpty()
                || mail == null || mail.isEmpty()
                || passport == null || passport.isEmpty();
    }

    public boolean isEmptyPassport() {
        return passport == null || passport.isEmpty();
    }

    public String getPassport() {
        return passport;
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

    public String getNewPassport() {
        return newPassport;
    }

    public String getBank() {
        return bank;
    }

    public void setPassport(String passport) {
        this.passport = passport;
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

    public void setNewPassport(String newPassport) {
        this.newPassport = newPassport;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
