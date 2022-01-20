package qiwi.model.input;

public class CreditInput {
    protected String limit = "";
    protected String interest = "";
    protected String bank = "";

    public boolean hasEmptyFields() {
        return limit == null || limit.isEmpty()
                || interest == null || interest.isEmpty()
                || bank == null || bank.isEmpty();
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

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
