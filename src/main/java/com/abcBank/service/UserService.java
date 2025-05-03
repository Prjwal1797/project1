package com.abcBank.service;

import com.abcBank.dto.BankResponse;
import com.abcBank.dto.CreditDebitRequest;
import com.abcBank.dto.EnquiryRequest;
import com.abcBank.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
	BankResponse balanceEnquiry(EnquiryRequest request);
	String nameEnquiry(EnquiryRequest request);
	
	BankResponse creditAccount(CreditDebitRequest request);
	BankResponse debitAccount(CreditDebitRequest request);
}
