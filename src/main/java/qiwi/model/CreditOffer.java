package qiwi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "credit_offers")
public class CreditOffer extends AbstractEntity {
    @Column(name = "credit_sum")
    private BigDecimal sum;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bank_id")
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
        this.sum = sum;
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
}
