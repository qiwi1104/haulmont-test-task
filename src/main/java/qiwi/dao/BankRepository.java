package qiwi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.Bank;

import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
}
