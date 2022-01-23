package qiwi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qiwi.model.CreditOffer;
import qiwi.model.Payment;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditOfferRepository extends JpaRepository<CreditOffer, UUID> {
}
