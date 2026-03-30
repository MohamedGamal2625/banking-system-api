package com.global.bankingsystemapi.Repository;

import com.global.bankingsystemapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction,Long> {
}
