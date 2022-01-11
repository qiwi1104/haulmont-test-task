package qiwi.model;

import java.time.LocalDate;

//@Entity
//@Table(name = "creditOffers")
public class CreditOffer extends AbstractEntity {
    private Client client;
    private Credit credit;
    private double sum;
    private Schedule schedule;

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
