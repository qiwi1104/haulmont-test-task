package qiwi.model;

import java.util.List;

//@Entity
//@Table(name = "banks")
public class Bank extends AbstractEntity {
    private String name;

    private List<Credit> creditList;
    private List<Client> clients;

    public String getName() {
        return name;
    }

    public List<Credit> getCreditList() {
        return creditList;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setName(String name) {
        this.name = name;
    }
}
