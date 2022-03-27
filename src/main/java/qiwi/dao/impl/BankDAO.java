package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.BankRepository;
import qiwi.model.Bank;
import qiwi.model.Client;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BankDAO {
    @Autowired
    private BankRepository repository;

    public void add(Bank bank) {
        repository.save(bank);
    }

    public void deleteById(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public void deleteClientById(UUID id) {
        for (Bank bank : repository.findAll()) {
            for (Client client : bank.getClients()) {
                if (client.getId().equals(id)) {
                    bank.getCreditOffers().removeIf(creditOffer -> creditOffer.getClient().equals(client));
                    bank.deleteClient(client);
                    repository.save(bank);
                    return;
                }

            }
        }
    }

    public Bank getBankByName(String name) {
        return repository.getBankByName(name);
    }

    public Bank getBankById(UUID id) {
        return repository.getBankById(id);
    }

    public boolean existsClientByPassport(String bankName, String passport) {
        for (Client client : getBankByName(bankName).getClients()) {
            if (client.getPassport().equals(passport)) {
                return true;
            }
        }

        return false;
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public boolean existsClientByBankName(String bankName, Client client) {
        return getBankByName(bankName).getClients().contains(client);
    }

    public void update(Bank oldBank, Bank newBank) {
        List<Bank> list = repository
                .findAll()
                .stream()
                .filter(b -> b.getId().equals(newBank.getId()))
                .collect(Collectors.toList());

        if (!list.isEmpty()) {
            oldBank.setName(newBank.getName());
        }

        repository.save(oldBank);
    }

    public List<Bank> findAll() {
        return repository.findAll();
    }
}
