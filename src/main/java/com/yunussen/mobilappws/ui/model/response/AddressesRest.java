package com.yunussen.mobilappws.ui.model.response;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressesRest  {
	private String addressId;
	private String city;
	private String country;
	private String streetName;
	private String postalCode;
	private String type;
}
