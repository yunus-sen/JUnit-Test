package com.yunussen.mobilappws.shared.dto;

import java.io.Serializable;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdressDto implements Serializable {

	
	private static final long serialVersionUID = -5606933658944257221L;
	
	private long id;
	private String addressId;
	private String city;
	private String country;
	private String streetName;
	private String postalCode;
	private String type;
	private UserDto userDetails;

	
}
