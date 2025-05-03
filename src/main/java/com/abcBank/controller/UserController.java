package com.abcBank.controller;

import com.abcBank.service.UserServiceImpl;
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
import com.abcBank.dto.UserRequest;
import com.abcBank.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	 @Autowired
	    UserService userService;

	    @PostMapping
	    public BankResponse createAccount(@RequestBody UserRequest userRequest){
	        return userService.createAccount(userRequest);
	    }

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
	    
	    

	    
}
