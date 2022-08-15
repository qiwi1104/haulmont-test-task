package qiwi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "CREDIT_OFFERS")
public class CreditOffer extends AbstractEntity {
    @Column(name = "CREDIT_SUM", precision = 20, scale = 5)
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

    public void calculatePayments(Integer months) {
        LocalDate date = LocalDate.now();

        BigDecimal monthlyInterest = credit
                .getInterest()
                .divide(BigDecimal.valueOf(100 * 12), RoundingMode.HALF_UP)
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal temp = monthlyInterest
                .divide(BigDecimal.ONE
                        .subtract(monthlyInterest
                                .add(BigDecimal.ONE)
                                .pow(-months, new MathContext(10))), RoundingMode.HALF_UP)
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal monthlyPaymentSum = sum
                .multiply(temp)
                .setScale(5, RoundingMode.HALF_UP);
        BigDecimal remainsCreditSum = sum;

        for (int i = 0; i < months; i++) {
            BigDecimal interestSum = remainsCreditSum
                    .multiply(monthlyInterest)
                    .setScale(5, RoundingMode.HALF_UP);
            BigDecimal creditSum = monthlyPaymentSum
                    .subtract(interestSum)
                    .setScale(5, RoundingMode.HALF_UP);

            Payment payment = new Payment(date, monthlyPaymentSum, creditSum, interestSum);
            this.addPayment(payment);
            payment.setCreditOffer(this);

            date = date.plusMonths(1);
            remainsCreditSum = remainsCreditSum
                    .subtract(creditSum)
                    .setScale(5, RoundingMode.HALF_UP);
        }
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
