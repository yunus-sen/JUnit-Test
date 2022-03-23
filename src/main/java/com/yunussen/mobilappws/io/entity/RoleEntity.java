package com.yunussen.mobilappws.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity implements Serializable {
 /**
	 * 
	 */
	private static final long serialVersionUID = -3925080892095469815L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(length = 20,nullable = false)
	private String name;
	@ManyToMany(mappedBy = "roles")
	private Collection<UserEntity>users;
	
	@ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
	@JoinTable(name = "roles_authorities"
			,joinColumns = @JoinColumn(name="roles_id",referencedColumnName = "id")
			,inverseJoinColumns =@JoinColumn(name="authorities_id",referencedColumnName = "id") )
	private Collection<AuthorityEntity> authorities;
	

	
}
