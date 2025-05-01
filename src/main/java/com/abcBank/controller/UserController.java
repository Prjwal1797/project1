package com.abcBank.controller;

import com.abcBank.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcBank.dto.BankResponse;
import com.abcBank.dto.UserRequest;
import com.abcBank.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserServiceImpl userServiceImpl;

	@Autowired
	UserService service;

	UserController(UserServiceImpl userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@PostMapping
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {

		return userServiceImpl.createAccount(userRequest);
	}

}
