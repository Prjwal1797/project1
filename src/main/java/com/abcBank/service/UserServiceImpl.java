package com.abcBank.service;
import lombok.Data;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abcBank.AbcBankApplication;
import com.abcBank.dto.AccountInfo;
import com.abcBank.dto.BankResponse;
import com.abcBank.dto.UserRequest;
import com.abcBank.entity.User;
import com.abcBank.repository.UserRepo;
import com.abcBank.util.AccUtil;

@Service
public class UserServiceImpl implements UserService {

    private final AbcBankApplication abcBankApplication;

	
	@Autowired
	UserRepo userRepo;

    UserServiceImpl(AbcBankApplication abcBankApplication) {
        this.abcBankApplication = abcBankApplication;
    }
	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/*
		 * creating an account - saving a new user into the db
		 * check if user already as account
		 */
		if(userRepo.existsByEmail(userRequest.getEMail())) {
		 	BankResponse response = BankResponse.builder()
					.responseCode(AccUtil.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccUtil.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		 	return response;
			
		}
		
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.otherName(userRequest.getOtherName())
				.address(userRequest.getAddress())
				.gender(userRequest.getGender())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accNum(AccUtil.generateAccNum())
				.email(userRequest.getEMail())
				.accBal(BigDecimal.ZERO)
				.phone(userRequest.getPhone())
				.alternatePhone(userRequest.getAlternatePhone())
				.status("Active").build();
			User savedUser = userRepo.save(newUser);
		
			return BankResponse.builder()
						.responseCode(AccUtil.ACCOUNT_CREATION_SUCCESS)
						.responseMessage(AccUtil.ACCOUNT_CREATION_MESSAGE)
						.accountInfo(AccountInfo.builder()
								.accountBalance(savedUser.getAccBal())
								.accountNumber(savedUser.getAccNum())
								.accountName(savedUser.getFirstName()+ " " + savedUser.getOtherName())
								.build())
						.build();
}
}