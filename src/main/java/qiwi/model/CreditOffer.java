package qiwi.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_offers")
public class CreditOffer extends AbstractEntity {
    @Column(name = "credit_sum")
    private double sum;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "schedule_date")),
            @AttributeOverride(name = "installmentSum", column = @Column(name = "schedule_installment_sum")),
            @AttributeOverride(name = "creditSum", column = @Column(name = "schedule_credit_sum")),
            @AttributeOverride(name = "interestSum", column = @Column(name = "schedule_interest_sum"))
    })
    private Schedule schedule;

    public double getSum() {
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSum(double sum) {
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
