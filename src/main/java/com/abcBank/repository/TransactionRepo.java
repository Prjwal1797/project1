package com.abcBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcBank.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, String> {

}
