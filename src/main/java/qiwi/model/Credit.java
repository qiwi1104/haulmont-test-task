package qiwi.model;

import qiwi.model.input.CreditInput;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credits")
public class Credit extends AbstractEntity {
    private double limit;
    private double interest;

    public Credit() {
    }

    public Credit(double limit, double interest) {
        this.limit = limit;
        this.interest = interest;
    }

    public Credit(CreditInput input) {
        this.limit = Double.parseDouble(input.getLimit());
        this.interest = Double.parseDouble(input.getInterest());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credit)) return false;

        Credit credit = (Credit) o;

        if (Double.compare(credit.limit, limit) != 0) return false;
        return Double.compare(credit.interest, interest) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(limit);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(interest);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
