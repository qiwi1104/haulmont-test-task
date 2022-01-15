package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.ClientRepository;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.CreditOffer;

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

    public void addCreditOffer(String passport, CreditOffer creditOffer) {
        getClientByPassport(passport).addCreditOffer(creditOffer);
    }

    public Client getClientByPassport(String passport) {
        return repository.getClientByPassport(passport);
    }

    public boolean existsByPassport(String passport) {
        return repository.existsByPassport(passport);
    }

    public List<Client> findAll() {
        return repository.findAll();
    }
}
