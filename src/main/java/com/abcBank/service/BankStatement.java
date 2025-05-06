package com.abcBank.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.abcBank.entity.Transaction;
import com.abcBank.repository.TransactionRepo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BankStatement {
	
	private TransactionRepo transactionRepo;
	
	
	/*
	 * retrieve list of transaction within a date range for a given account number
	 * generate a pdf file of transaction
	 * send the file via email
	 *  
	 */
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate){
		
			LocalDate start = LocalDate.parse(startDate , DateTimeFormatter.ISO_DATE);
			LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);		
		  List<Transaction> transactionList = transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				  		.filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();
		return transactionList ;
	}
	

}
