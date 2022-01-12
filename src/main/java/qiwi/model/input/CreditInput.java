package qiwi.model.input;

public class CreditInput {
    private String id;
    private String limit;
    private String interest;

    public boolean hasEmptyFields() {
        return limit == null || limit.isEmpty()
                || interest == null || interest.isEmpty();
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

    public void setId(String id) {
        this.id = id;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
