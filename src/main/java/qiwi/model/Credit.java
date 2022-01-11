package qiwi.model;

//@Entity
//@Table(name = "credits")
public class Credit extends AbstractEntity {
    private double limit;
    private double interest;

    public Credit() {
    }

    public Credit(double limit, double interest) {
        this.limit = limit;
        this.interest = interest;
    }

    public double getLimit() {
        return limit;
    }

    public double getInterest() {
        return interest;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
