package com.yunussen.mobilappws.ui.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsRequestModel {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<AdressRequestModel> addresses;
}
