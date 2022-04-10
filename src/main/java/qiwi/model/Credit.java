package qiwi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CREDITS")
public class Credit extends AbstractEntity {
    @Column(name = "LIMIT", precision = 20, scale = 5)
    private BigDecimal limit;
    @Column(name = "INTEREST", precision = 20, scale = 5)
    private BigDecimal interest;

    @ManyToOne
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreditOffer> creditOffers = new HashSet<>();

    public Credit() {
    }

    public Credit(BigDecimal limit, BigDecimal interest) {
        this.limit = limit.setScale(5, RoundingMode.HALF_UP);
        this.interest = interest.setScale(5, RoundingMode.HALF_UP);
    }

    public Credit(Bank bank, BigDecimal limit, BigDecimal interest) {
        this.limit = limit.setScale(5, RoundingMode.HALF_UP);
        this.interest = interest.setScale(5, RoundingMode.HALF_UP);

        this.bank = bank;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public Bank getBank() {
        return bank;
    }

    public Set<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit.setScale(5, RoundingMode.HALF_UP);
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest.setScale(5, RoundingMode.HALF_UP);
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credit)) return false;

        Credit credit = (Credit) o;

        if (limit.compareTo(credit.limit) != 0) return false;
        if (interest.compareTo(credit.interest) != 0) return false;
        return bank.equals(credit.bank);
    }

    @Override
    public int hashCode() {
        int result = limit.hashCode();
        result = 31 * result + interest.hashCode();
        result = 31 * result + bank.hashCode();
        return result;
    }
}
