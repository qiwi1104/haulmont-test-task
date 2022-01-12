package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.CreditRepository;
import qiwi.model.Credit;

import java.util.List;
import java.util.UUID;

@Service
public class CreditDAO {
    @Autowired
    private CreditRepository repository;

    public void add(Credit credit) {
        repository.save(credit);
    }

    public void delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public boolean exists(UUID id) {
        return repository.existsById(id);
    }

    public boolean exists(Credit credit) {
        return repository.findAll().contains(credit);
    }

    public Credit getCreditById(UUID id) {
        if (repository.existsById(id)) {
            return repository.getOne(id);
        }

        return null;
    }

    public Credit getCreditByPassportAndBank(String passport, String bank) {
        for (Credit credit : repository.findAll()) {
            if (credit.getClient().getPassport().equals(passport)
                    && credit.getBank().getName().equals(bank)) {
                return credit;
            }
        }

        return null;
    }

    public List<Credit> findAll() {
        return repository.findAll();
    }
}
