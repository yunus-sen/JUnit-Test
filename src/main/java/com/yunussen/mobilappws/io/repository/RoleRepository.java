package com.yunussen.mobilappws.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yunussen.mobilappws.io.entity.RoleEntity;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity, Long>{

	RoleEntity findByName(String name);
}
