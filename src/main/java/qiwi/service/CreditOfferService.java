package qiwi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.CreditOffer;
import qiwi.repository.CreditOfferRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CreditOfferService {
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
