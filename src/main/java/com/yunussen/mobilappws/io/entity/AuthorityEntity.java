package com.yunussen.mobilappws.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "authorities")
public class AuthorityEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5498288505745788746L;

	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	
	@Column(length = 20,nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "authorities")
	private Collection<RoleEntity>roles;
}
