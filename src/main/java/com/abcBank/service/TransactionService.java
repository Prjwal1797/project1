package com.abcBank.service;

import org.springframework.stereotype.Component;

import com.abcBank.dto.TransactionDto;
import com.abcBank.entity.Transaction;


@Component
public interface TransactionService {
	
	void saveTranscation(TransactionDto transcationDto);
	
	

}
