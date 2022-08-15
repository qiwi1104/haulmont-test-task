package qiwi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.Credit;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
    Credit getCreditById(UUID id);
}
