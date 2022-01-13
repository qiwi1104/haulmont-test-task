package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.ClientRepository;
import qiwi.model.Bank;
import qiwi.model.Client;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

@Service
public class ClientDAO {
    @Autowired
    private ClientRepository repository;

    public void add(Client client) {
        repository.save(client);
    }

    public void delete(UUID id) {
        if (repository.existsById(id)) {
            if (repository.getOne(id).getCredits().isEmpty()) {
                ListIterator<Bank> listIterator = repository.getOne(id).getBanks().listIterator();
                while (listIterator.hasNext()) {
                    listIterator.next().deleteClient(repository.getOne(id));
                    listIterator.remove();
                }

                repository.deleteById(id);
            }
        }
    }

    public boolean hasCredit(UUID id) {
        return !repository.getOne(id).getCredits().isEmpty();
    }

    public Client getClientByPassport(String passport) {
        for (Client client : repository.findAll()) {
            if (client.getPassport().equals(passport)) {
                return client;
            }
        }

        return null;
    }

    public boolean exists(Client client) {
        return getClientByPassport(client.getPassport()) != null;
    }

    public boolean existsByPassport(String passport) {
        return getClientByPassport(passport) != null;
    }

    public List<Client> findAll() {
        return repository.findAll();
    }
}
