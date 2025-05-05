package com.abcBank.controller;

import com.abcBank.service.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcBank.dto.BankResponse;
import com.abcBank.dto.CreditDebitRequest;
import com.abcBank.dto.EnquiryRequest;
import com.abcBank.dto.TransferRequest;
import com.abcBank.dto.UserRequest;
import com.abcBank.service.UserService;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
	
	 @Autowired
	    UserService userService;
	 
	 @Operation(
			 summary = "Create New User Account",
			 description = "Creating a new user and assigning an account ID"
			 )
	 @ApiResponse(
			 responseCode = "201",
			 description = "Http status 201 CREATED"
			 )
	    @PostMapping
	    public BankResponse createAccount(@RequestBody UserRequest userRequest){
	        return userService.createAccount(userRequest);
	    }
	 
	 
	 @Operation(
			 summary = "Balance Enquiry",
			 description = "Given an account number, check the balace of the account"
			 )
	 @ApiResponse(
			 responseCode = "200",
			 description = "Http status 201 SUCCESS"
			 )
	    @GetMapping("/balanceEnquiry")
	    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest  request){
	        return userService.balanceEnquiry(request);
	    }

	    @GetMapping("")
	    public String nameEnquiry(@RequestBody EnquiryRequest request){
	        return userService.nameEnquiry(request);
	    }
	    
	    @PostMapping("credit")
	    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
	    	return userService.creditAccount(request);
	    }
	    
	    @PostMapping("debit")
	    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
	    	return userService.debitAccount(request);
	    }
	    
	    @PostMapping("transfer")
	    
	    	public BankResponse transfer(@RequestBody TransferRequest request) {
	    		return userService.transfer(request);
	    

	    }
}
