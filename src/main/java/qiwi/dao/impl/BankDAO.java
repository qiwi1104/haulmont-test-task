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

    public void delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public void addClient(Bank bank, Client client) {
        if (!exists(bank.getName(), client)) {
            repository.getOne(bank.getId()).addClient(client);
        }
    }

    public void addCredit(Bank bank, Credit credit) {
        if (!exists(bank.getName(), credit)) {
            repository.getOne(bank.getId()).addCredit(credit);
        }
    }

    public void deleteClientById(UUID id) {
        for (Bank bank : repository.findAll()) {
            for (Client client : bank.getClients()) {
                if (client.getId().equals(id)) {
                    if (client.getCredits().isEmpty()) {
                        bank.deleteClient(client);
                        repository.save(bank);
                        return;
                    }
                }
            }
        }
    }

    public Bank getBankByName(String name) {
        for (Bank bank : repository.findAll()) {
            if (bank.getName().equals(name)) {
                return bank;
            }
        }

        return null;
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

    public boolean exists(Bank bank) {
        return getBankByName(bank.getName()) != null;
    }

    public boolean exists(String bankName, Client client) {
        return getBankByName(bankName).getClients().contains(client);
    }

    public boolean exists(String bankName, Credit credit) {
        return getBankByName(bankName).getCredits().contains(credit);
    }

    public List<Bank> findAll() {
        return repository.findAll();
    }
}
