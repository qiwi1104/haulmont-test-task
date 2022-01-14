package qiwi.model;

import qiwi.model.input.BankInput;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "banks")
public class Bank extends AbstractEntity {
    private String name;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Credit> credits;

    @ManyToMany
    @JoinTable(name = "clients_banks",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = {@JoinColumn(name = "client_id")})
    private Set<Client> clients;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreditOffer> creditOffers;

    public Bank() {
    }

    public Bank(String name) {
        this.name = name;
    }

    public Bank(BankInput input) {
        if (!input.getName().isEmpty()) {
            this.name = input.getName();
        }
    }

    public Bank(Set<Client> clients) {
        this.clients = new HashSet<>();
        this.clients.addAll(clients);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void deleteClient(Client client) {
        clients.remove(client);
    }

    public void addCredit(Credit credit) {
        credits.add(credit);
    }

    public void deleteCredit(Credit credit) {
        credits.remove(credit);
    }

    public String getName() {
        return name;
    }

    public Set<Credit> getCredits() {
        return credits;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Set<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;

        Bank bank = (Bank) o;

        return name.equals(bank.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
