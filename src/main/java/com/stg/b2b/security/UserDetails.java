package com.stg.b2b.security;
/**
 *JWT Security related operations.
 * 
 * @author Rahul Ravindra
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stg.b2b.repository.PermissionRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	public UserDetails(Long id, String username, String password,
					   Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	/**
	 * Building a UserDetails class based on input parameters
	 *
	 * @param userDto
	 * @return UserDetails
	 */
	@SuppressWarnings("deprecation")
	public static UserDetails build(UserDto userDto, List<String> permissionsByRole) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities = permissionsByRole.stream().map((permission) -> new SimpleGrantedAuthority(permission)).collect(Collectors.toList());
//		authorities.add(new SimpleGrantedAuthority(userDto.getRole().getRoleName()));
		return new UserDetails(
				(long) userDto.getUserId(),
				userDto.getUserDomainId(),
				"******",
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public Long getId() {
		return id;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetails user = (UserDetails) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}