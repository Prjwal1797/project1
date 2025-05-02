package com.abcBank.service;

import lombok.Data;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abcBank.AbcBankApplication;
import com.abcBank.dto.AccountInfo;
import com.abcBank.dto.BankResponse;
import com.abcBank.dto.CreditDebitRequest;
import com.abcBank.dto.EmailDetails;
import com.abcBank.dto.EnquiryRequest;
import com.abcBank.dto.UserRequest;
import com.abcBank.entity.User;
import com.abcBank.repository.UserRepo;
import com.abcBank.util.AccUtil;

@Service
public class UserServiceImpl implements UserService {

	private final AbcBankApplication abcBankApplication;

	@Autowired
	EmailServiceIml emailServiceIml;

	@Autowired
	UserRepo userRepo;

	@Autowired
	AccUtil accUtil;

	UserServiceImpl(AbcBankApplication abcBankApplication) {
		this.abcBankApplication = abcBankApplication;
	}

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/*
		 * creating an account - saving a new user into the db check if user already as
		 * account
		 */
		if (userRepo.existsByEmail(userRequest.getEMail())) {

			BankResponse response = BankResponse.builder().responseCode(AccUtil.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccUtil.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();
			return response;

		}

		User newUser = User.builder().firstName(userRequest.getFirstName()).otherName(userRequest.getOtherName())
				.address(userRequest.getAddress()).gender(userRequest.getGender())
				.stateOfOrigin(userRequest.getStateOfOrigin()).accNum(AccUtil.generateAccNum())
				.email(userRequest.getEMail()).accBal(BigDecimal.ZERO).phone(userRequest.getPhone())
				.alternatePhone(userRequest.getAlternatePhone()).status("Active").build();
		User savedUser = userRepo.save(newUser);

		// send mail alert
		EmailDetails emailDetails = EmailDetails.builder().recipients(savedUser.getEmail()).subject("ACCOUNT CREATION")
				.messageBody("YOUR ACCOUNT AS BEEN CREATED.\n Your Account Details: \n " + "Account Name: "
						+ savedUser.getFirstName() + " " + savedUser.getOtherName() + "\nAccount Number: "
						+ savedUser.getAccNum())

				.build();
		emailServiceIml.sendEmailAlert(emailDetails);

		return BankResponse.builder().responseCode(AccUtil.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccUtil.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(
						AccountInfo.builder().accountBalance(savedUser.getAccBal()).accountNumber(savedUser.getAccNum())
								.accountName(savedUser.getFirstName() + " " + savedUser.getOtherName()).build())
				.build();

	}
	// balance enquiry, name enquiry , credit , debit transfer

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		// check provided accNumber in dataBase
		boolean isAccountExist = userRepo.existsByAccNum(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(accUtil.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(accUtil.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		User foundUser = userRepo.findByAccNum(request.getAccountNumber());
		return BankResponse.builder().responseCode(AccUtil.ACCOUNT_EXISTS_CODE)
				.responseMessage(AccUtil.ACCOUNT_EXISTS_MESSAGE)

				.accountInfo(AccountInfo.builder().accountBalance(foundUser.getAccBal())
						.accountNumber(request.getAccountNumber())
						.accountName(foundUser.getFirstName() + " " + foundUser.getOtherName()).build())

				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		boolean isAccountExist = userRepo.existsByAccNum(request.getAccountNumber());
		if (!isAccountExist) {
			return AccUtil.ACCOUNT_NOT_EXIST_MESSAGE;

		}
		User foundUser = userRepo.findByAccNum(request.getAccountNumber());
		return foundUser.getFirstName() + " " + foundUser.getOtherName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		//checking if the account exists
		boolean isAccountExist = userRepo.existsByAccNum(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
					.responseCode(accUtil.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(accUtil.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null)
					.build();
		}
		
		User userToCredit = userRepo.findByAccNum(request.getAccountNumber());
		userToCredit.setAccBal(userToCredit.getAccBal().add(request.getAmount()));
		userToCredit.setAccBal(userToCredit.getAccBal().add(request.getAmount()));
		userRepo.save(userToCredit);
		return BankResponse.builder()
				.responseCode(AccUtil.AMOUNT_CREDITED_CODE)
				.responseMessage(AccUtil.AMOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName()+" "+ userToCredit.getOtherName())
						.accountBalance(userToCredit.getAccBal())
						.accountNumber(userToCredit.getAccNum())
						.build())
						.build();
	}
}