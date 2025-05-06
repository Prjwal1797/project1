package com.abcBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcBank.dto.TransactionDto;
import com.abcBank.entity.Transaction;
import com.abcBank.repository.TransactionRepo;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepo transactionRepo;
	
	
	@Override
	public void saveTranscation(TransactionDto transcationDto) {
		Transaction transaction = Transaction.builder()
				 .transcationType(transcationDto.getTranscationType())
				 .accountNumber(transcationDto.getAccountNumber())
				 .amount(transcationDto.getAmount())
				 .status("SUCCESS")
				.build();
	
		transactionRepo.save(transaction);
		System.out.println("Transcation saved sucessfully");
		
		
		
	}

}
