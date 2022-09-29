package com.employee.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.manage.entity.User;
import com.employee.manage.repository.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo repo;
	
	public User login(String email, String password) {
		User user = repo.findByEmail(email);
		return user;
			
		}
			
}