package com.yunussen.mobilappws.ui.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdressRequestModel {

	private String city;
	private String country;
	private String streetName;
	private String postalCode;
	private String type;
}
