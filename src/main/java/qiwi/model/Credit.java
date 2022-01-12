package qiwi.model;

import qiwi.model.input.CreditInput;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "credits")
public class Credit extends AbstractEntity {
    private double limit;
    private double interest;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public Credit() {
    }

    public Credit(double limit, double interest) {
        this.limit = limit;
        this.interest = interest;
    }

    public Credit(double limit, double interest, Bank bank, Client client) {
        this.limit = limit;
        this.interest = interest;

        this.bank = bank;
        this.client = client;
    }

    public Credit(CreditInput input) {
        this.limit = Double.parseDouble(input.getLimit());
        this.interest = Double.parseDouble(input.getInterest());
    }

    public Credit(CreditInput input, Bank bank, Client client) {
        this.limit = Double.parseDouble(input.getLimit());
        this.interest = Double.parseDouble(input.getInterest());
        this.bank = bank;
        this.client = client;
    }

    public double getLimit() {
        return limit;
    }

    public double getInterest() {
        return interest;
    }

    public Bank getBank() {
        return bank;
    }

    public Client getClient() {
        return client;
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
        if (Double.compare(credit.interest, interest) != 0) return false;
        if (!client.equals(credit.client)) return false;
        return bank.equals(credit.bank);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(limit);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(interest);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + client.hashCode();
        result = 31 * result + bank.hashCode();
        return result;
    }
}
