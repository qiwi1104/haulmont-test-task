package qiwi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "CREDIT_OFFERS")
public class CreditOffer extends AbstractEntity {
    @Column(name = "CREDIT_SUM")
    private BigDecimal sum;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CREDIT_ID")
    private Credit credit;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @OneToMany(mappedBy = "creditOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date")
    private SortedSet<Payment> payments;

    public CreditOffer() {
        this.payments = new TreeSet<>();
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    public BigDecimal getSum() {
        return sum;
    }

    public Credit getCredit() {
        return credit;
    }

    public Client getClient() {
        return client;
    }

    public Bank getBank() {
        return bank;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum.setScale(5, RoundingMode.HALF_UP);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditOffer that = (CreditOffer) o;

        if (sum.compareTo(that.sum) != 0) return false;
        if (!client.equals(that.client)) return false;
        if (!credit.equals(that.credit)) return false;
        return bank.equals(that.bank);
    }

    @Override
    public int hashCode() {
        int result = sum.hashCode();
        result = 31 * result + client.hashCode();
        result = 31 * result + credit.hashCode();
        result = 31 * result + bank.hashCode();
        return result;
    }
}
