package com.yunussen.mobilappws;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunussen.mobilappws.io.entity.AuthorityEntity;
import com.yunussen.mobilappws.io.entity.RoleEntity;
import com.yunussen.mobilappws.io.entity.UserEntity;
import com.yunussen.mobilappws.io.repository.AuthorityRepository;
import com.yunussen.mobilappws.io.repository.RoleRepository;
import com.yunussen.mobilappws.io.repository.UserRepository;
import com.yunussen.mobilappws.shared.Roles;
import com.yunussen.mobilappws.shared.Utils;

@Component
public class InitialUsersSetup {
	
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private Utils utils;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@EventListener
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {

		AuthorityEntity readAuthority=createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority=createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority=createAuthority("DELETE_AUTHORITY");
		
		createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority,writeAuthority));
		RoleEntity roleAdmin=createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority,writeAuthority,deleteAuthority));
		
		UserEntity user=userRepository.findByEmail("yunussen2727@gmail.com");
		if(roleAdmin==null||user!=null)return;
		
		user=new UserEntity();
		user.setFirstName("yunus");
		user.setLastName("Şen");
		user.setEmail("yunussen2727@gmail.com");
		user.setEmailVerificationStatus(Boolean.TRUE);		
		String publicUserId=utils.generateUserId(32);
		user.setUserId(publicUserId);
		user.setEncryptedPassword(bCryptPasswordEncoder.encode("1234"));
		user.setRoles(Arrays.asList(roleAdmin));
		userRepository.save(user);
	}
	
	@Transactional
	public RoleEntity createRole(String name,Collection<AuthorityEntity>authorities) {
		RoleEntity role=roleRepository.findByName(name);
		if(role==null) {
			role=new RoleEntity();
			role.setName(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);			
		}
		return role;
	}
	
	@Transactional
	public AuthorityEntity createAuthority(String name) {
		AuthorityEntity authority=authorityRepository.findByName(name);
		if(authority==null) {
			authority=new AuthorityEntity();
			authority.setName(name);
			authorityRepository.save(authority);			
		}
		return authority;
	}
}
