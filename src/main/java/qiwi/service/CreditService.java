package qiwi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.Credit;
import qiwi.repository.CreditRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CreditService {
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

    public Credit getCreditById(UUID id) {
        return repository.getCreditById(id);
    }

    public void update(Credit oldCredit, Credit newCredit) {
        oldCredit = getCreditById(oldCredit.getId());

        oldCredit.setLimit(newCredit.getLimit());
        oldCredit.setInterest(newCredit.getInterest());

        repository.save(oldCredit);
    }

    public List<Credit> findAll() {
        return repository.findAll();
    }
}
