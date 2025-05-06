package com.abcBank.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcBank.entity.Transaction;
import com.abcBank.service.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankstatement")
@AllArgsConstructor
public class TransactionController {

	private BankStatement bankStatement;
	
	
	@GetMapping
	public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate){
			
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
		
		
	}
	
}
