package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.ClientRepository;
import qiwi.model.Bank;
import qiwi.model.Client;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ClientDAO {
    @Autowired
    private ClientRepository repository;

    public void add(Client client) {
        repository.save(client);
    }

    public void deleteById(UUID id) {
        if (repository.existsById(id)) {
            Iterator<Bank> iterator = repository.getOne(id).getBanks().iterator();
            while (iterator.hasNext()) {
                iterator.next().deleteClient(repository.getOne(id));
                iterator.remove();
            }

            repository.deleteById(id);
        }
    }

    public Client getClientByPassport(String passport) {
        return repository.getClientByPassport(passport);
    }

    public Client getClientById(UUID id) {
        return repository.getClientById(id);
    }

    public boolean existsByPassport(String passport) {
        return repository.existsByPassport(passport);
    }

    public void update(Client oldClient, Client newClient) {
        oldClient = getClientById(oldClient.getId());

        oldClient.setFirstName(newClient.getFirstName());
        oldClient.setMiddleName(newClient.getMiddleName());
        oldClient.setLastName(newClient.getLastName());
        oldClient.setPhone(newClient.getPhone());
        oldClient.setMail(newClient.getMail());
        oldClient.setPassport(newClient.getPassport());

        repository.save(oldClient);
    }

    public List<Client> findAll() {
        return repository.findAll();
    }
}
