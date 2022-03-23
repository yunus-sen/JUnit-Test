package com.yunussen.mobilappws.io.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yunussen.mobilappws.io.entity.AddressEntity;
import com.yunussen.mobilappws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId);
	
}
