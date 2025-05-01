package com.abcBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcBank.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	
	Boolean existsByEmail(String email);


}
