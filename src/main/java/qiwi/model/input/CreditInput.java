package qiwi.model.input;

public class CreditInput {
    private String id;
    private String limit;
    private String interest;
    private String bank;
    private String passport;

    public boolean hasEmptyFields() {
        return limit == null || limit.isEmpty()
                || interest == null || interest.isEmpty()
                || bank == null || bank.isEmpty()
                || passport == null || passport.isEmpty();
    }

    public String getId() {
        return id;
    }

    public String getLimit() {
        return limit;
    }

    public String getInterest() {
        return interest;
    }

    public String getBank() {
        return bank;
    }

    public String getPassport() {
        return passport;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
