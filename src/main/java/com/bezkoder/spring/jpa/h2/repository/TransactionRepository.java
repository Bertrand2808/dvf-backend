package com.bezkoder.spring.jpa.h2.repository;

import com.bezkoder.spring.jpa.h2.business.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByIdMutation(String idMutation);

}
