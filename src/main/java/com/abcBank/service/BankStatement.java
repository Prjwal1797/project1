package com.abcBank.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Phaser;

import org.springframework.stereotype.Component;

import com.abcBank.dto.EmailDetails;
import com.abcBank.entity.Transaction;
import com.abcBank.entity.User;
import com.abcBank.repository.TransactionRepo;
import com.abcBank.repository.UserRepo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	private TransactionRepo transactionRepo;

	private static final String FILE = "E:\\sb_files\\MyStatement.pdf";
	
	private UserRepo UserRepo;
	
	private EmailService emailService;

	/*
	 * retrieve list of transaction within a date range for a given account number
	 * generate a pdf file of transaction send the file via email
	 * 
	 */

	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		List<Transaction> transactionList = transactionRepo.findAll().stream()
				.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();
		
		User user = UserRepo.findByAccNum(accountNumber);
		String customerName = user.getFirstName()+" "+ user.getOtherName();
		
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("setting of document");
		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("ABC Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		
		PdfPCell bankAddress = new PdfPCell(new Phrase("01, Banglore, Ind"));
		bankAddress.setBorder(0);
		
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);
		
		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell( new Phrase("Start Date: "+ startDate));
		customerInfo.setBorder(0);
		
		PdfPCell statement = new PdfPCell(new Phrase("Account Statement"));
		statement.setBorder(0);
		
		PdfPCell stopDate = new PdfPCell(new Phrase("End date: "+ endDate));
		stopDate.setBorder(0);
		
		PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+ customerName));
		name.setBorder(0);
		
		PdfPCell space = new PdfPCell();
		
		PdfPCell address = new PdfPCell(new Phrase("Customer Address: "+ user.getAddress()));
		address.setBorder(0);
		
		PdfPTable transactionTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBorder(0);
		transactionType.setBackgroundColor(BaseColor.BLUE);
		
		PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBorder(0);
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);
		
		transactionTable.addCell(date);
		transactionTable.addCell(transactionType);
		transactionTable.addCell(transactionAmount);
		transactionTable.addCell(status);
		
		
		transactionList.forEach(transaction ->{
			transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTranscationType()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus()));
			
			
			
		});
		
		
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(customerName);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionTable);
		
		document.close();
		
		EmailDetails emailDetails = EmailDetails.builder()
				.recipients(user.getEmail())
				.subject("ACCOUNT STATEMENT")
				.messageBody("KIndly find your requested account statement")
				.attachment(FILE)
				.build();
		
		emailService.sendEmailWithAttachment(emailDetails);
		
		return transactionList;
		
		
		
		
	}


		
	}


