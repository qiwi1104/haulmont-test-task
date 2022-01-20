package qiwi.model.input;

public class CreditEditInput extends CreditInput {
    private String newLimit = "";
    private String newInterest = "";

    public boolean hasEmptyFields() {
        return bank == null || bank.isEmpty();
    }

    public String getNewLimit() {
        return newLimit;
    }

    public String getNewInterest() {
        return newInterest;
    }

    public void setNewLimit(String newLimit) {
        this.newLimit = newLimit;
    }

    public void setNewInterest(String newInterest) {
        this.newInterest = newInterest;
    }
}
