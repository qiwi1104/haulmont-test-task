package qiwi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment extends AbstractEntity implements Comparable<Payment> {
    @Column(name = "payment_date")
    private LocalDate date;
    @Column(name = "payment_sum")
    private BigDecimal paymentSum;
    @Column(name = "credit_sum")
    private BigDecimal creditSum;
    @Column(name = "interest_sum")
    private BigDecimal interestSum;
    @ManyToOne
    @JoinColumn(name = "credit_offer_id")
    private CreditOffer creditOffer;

    public Payment() {
    }

    public Payment(LocalDate date, BigDecimal paymentSum, BigDecimal creditSum, BigDecimal interestSum) {
        this.date = date;
        this.paymentSum = paymentSum.setScale(2, RoundingMode.HALF_UP);
        this.creditSum = creditSum.setScale(2, RoundingMode.HALF_UP);
        this.interestSum = interestSum.setScale(2, RoundingMode.HALF_UP);
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
        if (!paymentSum.equals(payment.paymentSum)) return false;
        if (!creditSum.equals(payment.creditSum)) return false;
        if (!interestSum.equals(payment.interestSum)) return false;
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
