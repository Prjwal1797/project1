package com.abcBank.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountInfo {
	
	@Schema(
			name = "User Account Name"
			)
	private String accountName;
	@Schema(
			name = "User Account Balance"
			)
	private BigDecimal accountBalance;
	@Schema(
			name = "User Account Number"
			)
	private String accountNumber;

}
