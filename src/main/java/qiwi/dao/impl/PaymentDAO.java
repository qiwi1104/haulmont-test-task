package qiwi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.Payment;

import java.util.List;

@Service
public class PaymentDAO {
    @Autowired
    private PaymentRepository repository;

    public void add(Payment payment) {
        repository.save(payment);
    }

    public List<Payment> findAll() {
        return repository.findAll();
    }
}
