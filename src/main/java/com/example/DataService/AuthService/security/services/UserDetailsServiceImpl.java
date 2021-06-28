package com.example.DataService.AuthService.security.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.DataService.AuthService.models.User;
import com.repository.UserRepository;
 
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		/*Fetches a user from the database and returns a UserDetailsImpl object. T
		 * This is an object that has all the user information in addition to a list of authorities(Roles)*/
		User user = userRepository.findByUsername(username).orElseThrow();
		return UserDetailsImpl.build(user);
	}
}
