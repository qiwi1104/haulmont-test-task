package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.dao.CreditOfferRepository;
import qiwi.model.CreditOffer;

import java.util.List;

@Service
public class CreditOfferDAO {
    @Autowired
    private CreditOfferRepository repository;

    public List<CreditOffer> findAll() {
        return repository.findAll();
    }
}
