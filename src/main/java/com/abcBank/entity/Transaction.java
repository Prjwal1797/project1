package com.abcBank.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table (name = "Transactions")
public class Transaction {

		@Id
		@GeneratedValue(strategy = GenerationType.UUID)
		private String transcationId;
		private String transcationType;
		private BigDecimal amount;
		private String accountNumber;
		private String status;
	}
	

