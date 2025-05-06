package com.abcBank.service;

import lombok.Data;

import java.lang.module.ModuleDescriptor.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abcBank.AbcBankApplication;
import com.abcBank.dto.AccountInfo;
import com.abcBank.dto.BankResponse;
import com.abcBank.dto.CreditDebitRequest;
import com.abcBank.dto.EmailDetails;
import com.abcBank.dto.EnquiryRequest;
import com.abcBank.dto.TransactionDto;
import com.abcBank.dto.TransferRequest;
import com.abcBank.dto.UserRequest;
import com.abcBank.entity.Transaction;
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
	
	@Autowired
	TransactionService transcationService;
	


	
	
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
		// checking if the account exists
		boolean isAccountExist = userRepo.existsByAccNum(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(accUtil.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(accUtil.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		User userToCredit = userRepo.findByAccNum(request.getAccountNumber());
		userToCredit.setAccBal(userToCredit.getAccBal().add(request.getAmount()));
		userRepo.save(userToCredit);
		
		//save transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(userToCredit.getAccNum())
				.transcationType("CREDIT")
				.amount(request.getAmount())
				.build();
		
        transcationService.saveTranscation(transactionDto);
		
		return BankResponse.builder().responseCode(AccUtil.AMOUNT_CREDITED_CODE)
				.responseMessage(AccUtil.AMOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName() + " " + userToCredit.getOtherName())
						.accountBalance(userToCredit.getAccBal()).accountNumber(userToCredit.getAccNum()).build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		/*
		 * check if the account is present check the amount to be withdraw is not more
		 * than the balance.
		 */
		boolean isAccountExist = userRepo.existsByAccNum(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(accUtil.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(accUtil.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		
		
        
		
		
		User userToDebit = userRepo.findByAccNum(request.getAccountNumber());
		BigDecimal availableBalance = userToDebit.getAccBal();
		BigDecimal debitAmount = request.getAmount();
		if (availableBalance.compareTo(debitAmount) < 0) {
			return BankResponse.builder().responseCode(accUtil.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(accUtil.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();

		}
		
		//SAVE DEBIT TRANSACTION
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(userToDebit.getAccNum())
				.transcationType("DEBIT")
				.amount(request.getAmount())
				.build();
		transcationService.saveTranscation(transactionDto);
		
		
		userToDebit.setAccBal(userToDebit.getAccBal().subtract(request.getAmount()));
		userRepo.save(userToDebit);
		return BankResponse.builder().responseCode(accUtil.AMOUNT_DEBIT_SUCCESS_CODE)
				.responseMessage(accUtil.AMOUNT_DEBIT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountNumber(request.getAccountNumber())
						.accountName(userToDebit.getFirstName() + " " + userToDebit.getOtherName())
						.accountBalance(userToDebit.getAccBal()).build())
				.build();

	}

	@Override
	public BankResponse transfer(TransferRequest request) {
		/*
		 * get the accoun to debit; (check if account is exists check if the amount iam
		 * debiting is not more than current balance debit the account get the account
		 * to credit credit he account
		 */

		boolean isDestinationAccountExist = userRepo.existsByAccNum(request.getDestinationAccountNumber());

		if (!isDestinationAccountExist) {

			return BankResponse.builder()
					.responseCode(accUtil.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(accUtil.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}

		User sourceAccountUser = userRepo.findByAccNum(request.getSourceAccountNumber());
		if (request.getAmount().compareTo(sourceAccountUser.getAccBal())> 0 ) {
			
			return BankResponse.builder()
					.responseCode(accUtil.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(accUtil.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
			
		}
		

		sourceAccountUser.setAccBal(sourceAccountUser.getAccBal().subtract(request.getAmount()));
		String sourceUserName = sourceAccountUser.getFirstName();
		userRepo.save(sourceAccountUser);

		EmailDetails debitAlert = EmailDetails.builder().subject("Amount Debit Alert")
				.recipients(sourceAccountUser.getEmail())
				.messageBody("The sum of " + request.getAmount()
						+ "has been deducted from your account! Your current account balance is "
						+ sourceAccountUser.getAccBal())
				.build();
		emailServiceIml.sendEmailAlert(debitAlert);

		User destinationAccountUser = userRepo.findByAccNum(request.getDestinationAccountNumber());
		destinationAccountUser.setAccBal(destinationAccountUser.getAccBal().add(request.getAmount()));
		
		//String recipientUser = destinationAccountUser.getFirstName();
		userRepo.save(destinationAccountUser);
		EmailDetails creditAlert = EmailDetails.builder().subject("Amount Credit Alert")
				.recipients(destinationAccountUser.getEmail())
				.messageBody("The sum of " + request.getAmount() + " has been Credited from "
						+ sourceAccountUser.getFirstName() + "  ! Your current account balance is "
						+ sourceAccountUser.getAccBal())
				.build();
		emailServiceIml.sendEmailAlert(creditAlert);
		
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccNum())
				.transcationType("DEBIT")
				.amount(request.getAmount())
				//.createdAt(LocalDate.now())
				.build();
		transcationService.saveTranscation(transactionDto);
		
		
		return BankResponse.builder()
				.responseCode(AccUtil.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(accUtil.TRANSFER_SUCCESSFUL_MESSAGE)
				.accountInfo(null)
				.build();
	}

}
