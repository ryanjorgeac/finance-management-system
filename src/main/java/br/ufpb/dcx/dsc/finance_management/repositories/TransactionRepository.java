package br.ufpb.dcx.dsc.finance_management.repositories;

import br.ufpb.dcx.dsc.finance_management.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}
