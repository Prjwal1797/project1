package com.abcBank.service;

import com.abcBank.dto.BankResponse;
import com.abcBank.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
}
