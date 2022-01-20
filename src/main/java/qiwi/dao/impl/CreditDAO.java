package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.CreditRepository;
import qiwi.model.Bank;
import qiwi.model.Credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CreditDAO {
    @Autowired
    private CreditRepository repository;

    public void add(Credit credit) {
        repository.save(credit);
    }

    public void deleteById(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public boolean exists(Credit credit) {
        return repository.findAll().contains(credit);
    }

    public Credit getCredit(String bankName, BigDecimal limit, BigDecimal interest) {
        Bank bank = new Bank(bankName);
        Credit creditToFind = new Credit(limit, interest, bank);

        for (Credit credit : repository.findAll()) {
            if (credit.equals(creditToFind)) {
                return credit;
            }
        }

        return null;
    }

    public List<Credit> findAll() {
        return repository.findAll();
    }
}
