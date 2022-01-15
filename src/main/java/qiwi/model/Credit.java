package qiwi.model;

import qiwi.model.input.CreditInput;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credits")
public class Credit extends AbstractEntity {
    private BigDecimal limit;
    private BigDecimal interest;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreditOffer> creditOffers = new HashSet<>();

    public Credit() {
    }

    public Credit(BigDecimal limit, BigDecimal interest) {
        this.limit = limit.setScale(2, RoundingMode.HALF_UP);
        this.interest = interest.setScale(2, RoundingMode.HALF_UP);
    }

    public Credit(BigDecimal limit, BigDecimal interest, Bank bank) {
        this.limit = limit.setScale(2, RoundingMode.HALF_UP);
        this.interest = interest.setScale(2, RoundingMode.HALF_UP);

        this.bank = bank;
    }

    public Credit(CreditInput input) {
        this.limit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));
        this.interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));
    }

    public Credit(CreditInput input, Bank bank) {
        this.limit = BigDecimal.valueOf(Double.parseDouble(input.getLimit()));
        this.interest = BigDecimal.valueOf(Double.parseDouble(input.getInterest()));
        this.bank = bank;
    }

    public void addCreditOffer(CreditOffer creditOffer) {
        this.creditOffers.add(creditOffer);
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
        this.limit = limit.setScale(2, RoundingMode.HALF_UP);
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest.setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credit)) return false;

        Credit credit = (Credit) o;

        if (!limit.equals(credit.limit)) return false;
        if (!interest.equals(credit.interest)) return false;
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
