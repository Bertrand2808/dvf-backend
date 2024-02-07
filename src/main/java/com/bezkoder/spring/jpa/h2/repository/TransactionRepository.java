package com.bezkoder.spring.jpa.h2.repository;

import com.bezkoder.spring.jpa.h2.business.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
