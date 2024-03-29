package qiwi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "PAYMENTS")
public class Payment extends AbstractEntity implements Comparable<Payment> {
    @Column(name = "PAYMENT_DATE")
    private LocalDate date;
    @Column(name = "PAYMENT_SUM")
    private BigDecimal paymentSum;
    @Column(name = "CREDIT_SUM")
    private BigDecimal creditSum;
    @Column(name = "INTEREST_SUM")
    private BigDecimal interestSum;
    @ManyToOne
    @JoinColumn(name = "CREDIT_OFFER_ID")
    private CreditOffer creditOffer;

    public Payment() {
    }

    public Payment(LocalDate date, BigDecimal paymentSum, BigDecimal creditSum, BigDecimal interestSum) {
        this.date = date;
        this.paymentSum = paymentSum
                .setScale(5, RoundingMode.HALF_UP);
        this.creditSum = creditSum
                .setScale(5, RoundingMode.HALF_UP);
        this.interestSum = interestSum
                .setScale(5, RoundingMode.HALF_UP);
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPaymentSum() {
        return paymentSum;
    }

    public BigDecimal getCreditSum() {
        return creditSum;
    }

    public BigDecimal getInterestSum() {
        return interestSum;
    }

    public CreditOffer getCreditOffer() {
        return creditOffer;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPaymentSum(BigDecimal paymentSum) {
        this.paymentSum = paymentSum;
    }

    public void setCreditSum(BigDecimal creditSum) {
        this.creditSum = creditSum;
    }

    public void setInterestSum(BigDecimal interestSum) {
        this.interestSum = interestSum;
    }

    public void setCreditOffer(CreditOffer creditOffer) {
        this.creditOffer = creditOffer;
    }

    @Override
    public int compareTo(Payment o) {
        if (this.date.isBefore(o.getDate())) {
            return -1;
        }
        if (this.date.isEqual(o.getDate())) {
            return 0;
        }
        return this.date.isAfter(o.getDate()) ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;

        Payment payment = (Payment) o;

        if (!date.equals(payment.date)) return false;
        if (paymentSum.compareTo(payment.paymentSum) != 0) return false;
        if (creditSum.compareTo(payment.creditSum) != 0) return false;
        if (interestSum.compareTo(payment.interestSum) != 0) return false;
        return creditOffer.equals(payment.creditOffer);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + paymentSum.hashCode();
        result = 31 * result + creditSum.hashCode();
        result = 31 * result + interestSum.hashCode();
        result = 31 * result + creditOffer.hashCode();
        return result;
    }
}
