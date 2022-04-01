package qiwi.model;

import qiwi.model.input.ClientInput;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "CLIENTS")
public class Client extends AbstractEntity {
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "MAIL")
    private String mail;
    @Column(name = "PASSPORT")
    @NotBlank(message = "Passport field must not be empty.")
    private String passport;

    @ManyToMany(mappedBy = "clients")
    private Set<Bank> banks;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreditOffer> creditOffers;

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

    public void addCreditOffer(CreditOffer creditOffer) {
        creditOffers.add(creditOffer);
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

    public Set<CreditOffer> getCreditOffers() {
        return creditOffers;
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
        String thisPhone = new StringBuilder(this.phone).reverse().substring(0, 10);
        phone = new StringBuilder(phone).reverse().substring(0, 10);

        return thisPhone.contains(phone);
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
