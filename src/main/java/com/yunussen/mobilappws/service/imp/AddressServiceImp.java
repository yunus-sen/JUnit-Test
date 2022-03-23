package com.yunussen.mobilappws.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunussen.mobilappws.io.entity.AddressEntity;
import com.yunussen.mobilappws.io.entity.UserEntity;
import com.yunussen.mobilappws.io.repository.AddressRepository;
import com.yunussen.mobilappws.io.repository.UserRepository;
import com.yunussen.mobilappws.service.AddressService;
import com.yunussen.mobilappws.shared.dto.AdressDto;

@Service
public class AddressServiceImp implements AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AdressDto> getAddresses(String userId) {
        List<AdressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity==null) return returnValue;
 
        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity:addresses)
        {
            returnValue.add( modelMapper.map(addressEntity, AdressDto.class) );
        }
        
        return returnValue;
	}

	@Override
	public AdressDto getAddress(String addressId) {
		AdressDto returnValue = null;

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        
        if(addressEntity!=null)
        {
            returnValue = new ModelMapper().map(addressEntity, AdressDto.class);
        }
 
        return returnValue;
	}

}
