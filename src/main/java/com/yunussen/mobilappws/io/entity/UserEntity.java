package com.yunussen.mobilappws.io.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.ManyToAny;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id","email"})
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
	
	private static final long serialVersionUID = -7316261795657715275L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false , length = 50)
	private String firstName;
	
	@Column(nullable = false , length = 50)
	private String lastName;
	
	@Column(nullable = false , length = 120,unique = true)
	private String email;
	
	@Column(nullable = false )
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	@Column(nullable = false )
	private Boolean emailVerificationStatus=false;
	
	
	@OneToMany(mappedBy = "userDetails",cascade = CascadeType.ALL)
	private List<AddressEntity> addresses;
	
	@ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles"
			,joinColumns = @JoinColumn(name="users_id",referencedColumnName = "id")
			,inverseJoinColumns =@JoinColumn(name="roles_id",referencedColumnName = "id") )
	private Collection<RoleEntity> roles;
	
	

}
