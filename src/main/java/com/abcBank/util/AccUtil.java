package com.abcBank.util;

import java.security.PublicKey;
import java.time.Year;

import org.springframework.stereotype.Service;

@Service
public class AccUtil {
	
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user had created  account already";
	public static final String ACCOUNT_CREATION_SUCCESS ="002";
	public static final String ACCOUNT_CREATION_MESSAGE ="ACCOUNT CREATED SUCCESSFULLY";
	public static final String ACCOUNT_NOT_EXIST_CODE ="003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE ="USER WITH THE PROVIDED ACCOUNT NUMBER IS NOT EXSIST";
	public static final String AMOUNT_CREDITED_CODE ="004";
	public static final String AMOUNT_CREDITED_SUCCESS_MESSAGE ="AMOUNT HAS BEEN CREDITED TO YOUR ACCOUNT";
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "YOUR ACCOUNT BALANCE IS INSUFFICIENT";
	public static final String AMOUNT_DEBIT_SUCCESS_CODE = "007";
	public static final String AMOUNT_DEBIT_SUCCESS_MESSAGE = "AMOUNT HAS BEEN DEBITED";
	

	public static String generateAccNum() {

		/*
		 * 2025 + random six digits
		 */
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;
		int randomNum = (int) Math.floor(Math.random() * (max - min + 1) + min);

		// convert current year and accNum into string
		String year = String.valueOf(currentYear);
		String number = String.valueOf(randomNum);
		StringBuilder accountNumber = new StringBuilder();

		return accountNumber.append(year).append(number).toString();
	}
}
