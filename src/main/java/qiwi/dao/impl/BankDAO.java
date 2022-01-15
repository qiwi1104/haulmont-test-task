package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.BankRepository;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.Credit;

import java.util.List;
import java.util.UUID;

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

    public void addClient(Bank bank, Client client) {
        if (!existsClientByBankName(bank.getName(), client)) {
            repository.getOne(bank.getId()).addClient(client);
        }
    }

    public void addCredit(Bank bank, Credit credit) {
        if (!existsCreditByBankName(bank.getName(), credit)) {
            repository.getOne(bank.getId()).addCredit(credit);
        }
    }

    public void deleteClientById(UUID id) {
        for (Bank bank : repository.findAll()) {
            for (Client client : bank.getClients()) {
                if (client.getId().equals(id)) {
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

    public Client getClientByPassport(String passport) {
        for (Bank bank : repository.findAll()) {
            for (Client client : bank.getClients()) {
                if (client.getPassport().equals(passport)) {
                    return client;
                }
            }
        }

        return null;
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public boolean existsClientByBankName(String bankName, Client client) {
        return getBankByName(bankName).getClients().contains(client);
    }

    public boolean existsCreditByBankName(String bankName, Credit credit) {
        return getBankByName(bankName).getCredits().contains(credit);
    }

    public List<Bank> findAll() {
        return repository.findAll();
    }
}
