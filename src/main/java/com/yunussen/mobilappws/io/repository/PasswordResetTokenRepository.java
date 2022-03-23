package com.yunussen.mobilappws.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunussen.mobilappws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

	 //PasswordResetTokenEntity save(PasswordResetTokenEntity  passwordResetTokenEntity);
	 
	 PasswordResetTokenEntity findByToken(String token);
}
