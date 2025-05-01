package com.abcBank.util;

import java.security.PublicKey;
import java.time.Year;

public class AccUtil {
	
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user had created  account already";
	public static final String ACCOUNT_CREATION_SUCCESS ="002";
	public static final String ACCOUNT_CREATION_MESSAGE ="ACCOUNT CREATED SUCCESSFULLY";
	

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
