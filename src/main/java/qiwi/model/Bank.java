package qiwi.model;

import qiwi.model.input.BankInput;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banks")
public class Bank extends AbstractEntity {
    private String name;

    @OneToMany(mappedBy = "bank")
    private List<Credit> credits;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "clients_banks",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = {@JoinColumn(name = "bank_id")})
    private List<Client> clients;

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

    public Bank(List<Client> clients) {
        this.clients = new ArrayList<>();
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

    public List<Credit> getCredits() {
        return credits;
    }

    public List<Client> getClients() {
        return clients;
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
