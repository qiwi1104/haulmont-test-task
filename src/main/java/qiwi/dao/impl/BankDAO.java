package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.BankRepository;
import qiwi.model.Bank;
import qiwi.model.Client;

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

    public void deleteClientById(UUID id) {
        for (Bank bank : repository.findAll()) {
            for (Client client : bank.getClients()) {
                if (client.getId().equals(id)) {
                    bank.deleteClient(client);
                    repository.save(bank);
                    break;
                }
            }
        }
    }

//    public void deleteCreditById(UUID id) {
//        for (Bank bank : repository.findAll()) {
//            for (Credit credit : bank.getCredits()) {
//                if (credit.getId().equals(id)) {
//                    bank.deleteClient(credit);
//                    repository.save(bank);
//                    break;
//                }
//            }
//        }
//    }

    public Bank getBankByName(String name) {
        for (Bank bank : repository.findAll()) {
            if (bank.getName().equals(name)) {
                return bank;
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

    public List<Bank> findAll() {
        return repository.findAll();
    }
}
