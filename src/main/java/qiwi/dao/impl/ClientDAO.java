package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.ClientRepository;
import qiwi.model.Client;

import java.util.List;
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
            repository.deleteById(id);
        }
    }

    public Client getClientById(String id) {
        UUID uuid = UUID.fromString(id);
        return repository.existsById(uuid)
                ? repository.getOne(uuid)
                : null;
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

    public List<Client> findAll() {
        return repository.findAll();
    }
}
