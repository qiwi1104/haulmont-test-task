package qiwi.model.input;

public class CreditOfferInput {
    private String limit = "";
    private String interest = "";
    private String bank = "";
    private String passport = "";
    private String months = "";
    private String sum = "";

    public boolean hasEmptyFields() {
        return limit == null || limit.isEmpty()
                || interest == null || interest.isEmpty()
                || bank == null || bank.isEmpty()
                || passport == null || passport.isEmpty()
                || months == null || months.isEmpty()
                || sum == null || sum.isEmpty();
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

    public String getMonths() {
        return months;
    }

    public String getSum() {
        return sum;
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

    public void setMonths(String months) {
        this.months = months;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}
