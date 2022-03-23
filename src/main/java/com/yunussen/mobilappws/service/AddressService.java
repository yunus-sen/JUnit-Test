package com.yunussen.mobilappws.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunussen.mobilappws.shared.dto.AdressDto;


public interface AddressService {
	List<AdressDto> getAddresses(String userId);
    AdressDto getAddress(String addressId);
}
