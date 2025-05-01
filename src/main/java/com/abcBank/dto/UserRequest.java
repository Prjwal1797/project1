package com.abcBank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	private String firstName;
	private String otherName;
	private String gender;
	private String address;
	private String stateOfOrigin;
	private String eMail;
	private String phone;
	private String alternatePhone;
	
}
