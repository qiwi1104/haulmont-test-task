package qiwi.model.input;

public class AddClientBankInput {
    private String passport = "";
    private String bank = "";

    public boolean hasEmptyFields() {
        return passport == null || passport.isEmpty()
                || bank == null || bank.isEmpty();
    }

    public String getPassport() {
        return passport;
    }

    public String getBank() {
        return bank;
    }
}
