package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.CreditOfferRepository;
import qiwi.model.CreditOffer;
import qiwi.model.Payment;

import java.util.List;
import java.util.UUID;

@Service
public class CreditOfferDAO {
    @Autowired
    private CreditOfferRepository repository;

    public void add(CreditOffer creditOffer) {
        repository.save(creditOffer);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean exists(CreditOffer creditOffer) {
        return repository.findAll().contains(creditOffer);
    }

    public List<CreditOffer> findAll() {
        return repository.findAll();
    }
}
