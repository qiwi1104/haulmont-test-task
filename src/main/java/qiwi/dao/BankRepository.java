package qiwi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.Bank;

import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
    boolean existsByName(String name);

    Bank getBankByName(String name);

    Bank getBankById(UUID id);
}
