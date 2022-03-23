package com.yunussen.mobilappws.shared.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

	
	private static final long serialVersionUID = 3369841465522683236L;
	private long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String encryptedPassword;
	private String emailVerificationToken;
	private Boolean emailVerificationStatus=false;
	private List<AdressDto>addresses;
	//private Collection<String>roles;
	private String role;
	
}
