package com.yunussen.mobilappws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.yunussen.mobilappws.shared.dto.UserDto;

public interface UserService extends UserDetailsService{

	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	UserDto findUserByid(String id);
	UserDto updateUser(String userId,UserDto user);
	void delete(String userId);
	List<UserDto>getUsers(int page,int limit);
	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String email);
	 boolean resetPassword(String token, String password);
	
	
}
