package qiwi.model;

import javax.persistence.*;
import java.time.LocalDate;

//@Entity
//@Table(name = "credit_offers")
public class CreditOffer extends AbstractEntity {
    private Client client;
    private Credit credit;
    private double sum;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "schedule_date")),
            @AttributeOverride(name = "installmentSum", column = @Column(name = "schedule_installment_sum")),
            @AttributeOverride(name = "creditSum", column = @Column(name = "schedule_credit_sum")),
            @AttributeOverride(name = "interestSum", column = @Column(name = "schedule_interest_sum"))
    })
    private Schedule schedule;

    @Embeddable
    private static class Schedule {
        private LocalDate date;
        private double installmentSum;
        private double creditSum;
        private double interestSum;

        public Schedule() {
        }

        public Schedule(LocalDate date, double installmentSum, double creditSum, double interestSum) {
            this.date = date;
            this.installmentSum = installmentSum;
            this.creditSum = creditSum;
            this.interestSum = interestSum;
        }

        public LocalDate getDate() {
            return date;
        }

        public double getInstallmentSum() {
            return installmentSum;
        }

        public double getCreditSum() {
            return creditSum;
        }

        public double getInterestSum() {
            return interestSum;
        }
    }
}
