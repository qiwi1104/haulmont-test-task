package qiwi.model;

import qiwi.model.input.ClientInput;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client extends AbstractEntity {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String mail;
    private String passport;

    @ManyToMany(mappedBy = "clients")
    private Set<Bank> banks;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Credit> credits;

    public Client() {
    }

    public Client(String firstName, String middleName, String lastName, String phone, String mail, String passport) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.mail = mail;
        this.passport = passport;
    }

    public Client(ClientInput input) {
        if (input.getFirstName() != null) {
            this.firstName = input.getFirstName();
        }
        if (input.getMiddleName() != null) {
            this.middleName = input.getMiddleName();
        }
        if (input.getLastName() != null) {
            this.lastName = input.getLastName();
        }
        if (input.getPhone() != null) {
            this.phone = input.getPhone();
        }
        if (input.getMail() != null) {
            this.mail = input.getMail();
        }
        if (input.getPassport() != null) {
            this.passport = input.getPassport();
        }
    }

    public void addBank(Bank bank) {
        banks.add(bank);
    }

    public void addCredit(Credit credit) {
        credits.add(credit);
    }

    public void deleteBank(Bank bank) {
        banks.remove(bank);
    }

    public void deleteCredit(Credit credit) {
        credits.remove(credit);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getPassport() {
        return passport;
    }

    public Set<Bank> getBanks() {
        return banks;
    }

    public Set<Credit> getCredits() {
        return credits;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public boolean equalsPhone(String phone) {
        String thisReversedPhone = new StringBuilder(this.phone).reverse().substring(0, 10);
        String reversedPhone = new StringBuilder(phone).reverse().substring(0, 10);
        return thisReversedPhone.equals(reversedPhone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        return passport.equals(client.passport);
    }

    @Override
    public int hashCode() {
        return passport.hashCode();
    }
}
