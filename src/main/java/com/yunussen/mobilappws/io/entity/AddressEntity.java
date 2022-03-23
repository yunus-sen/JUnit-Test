package com.yunussen.mobilappws.io.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.yunussen.mobilappws.shared.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity implements Serializable{
	private static final long serialVersionUID = -6155330834070537875L;
	
	@Id
	@GeneratedValue
	private long id;
	@Column(nullable = false)
	private String addressId;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	private String country;
	@Column(nullable = false)
	private String streetName;
	@Column(nullable = false)
	private String postalCode;
	@Column(nullable = false)
	private String type;
	@JoinColumn(name = "user_id")
	@ManyToOne()
	private UserEntity userDetails;
	
	

}
