package com.abcBank.service;

import com.abcBank.dto.EmailDetails;

public interface EmailService {
	
	void sendEmailAlert(EmailDetails emailDetails);

}
