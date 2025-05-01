package com.abcBank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountInfo {
	
	private String accountName;
	private BigDecimal accountBalance;
	private String accountNumber;

}
