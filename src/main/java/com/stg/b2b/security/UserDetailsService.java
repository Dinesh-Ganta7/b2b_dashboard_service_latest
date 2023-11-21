package com.stg.b2b.security;

import com.stg.b2b.entity.User;
import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.repository.PermissionRepository;
import com.stg.b2b.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * JWT Security related operations.
 * 
 * @author Rahul Ravindra
 *
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
    UserRepository userRepository;

	@Autowired
	PermissionRepository permissionRepository;


	/**
	 * Checks whether the user exists in the database
	 *
	 * @param username the username identifying the user whose data is required.
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	@Transactional
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = fetchByUsername(username);
				if(userDto.getUserDomainId()== null) {
					throw new NotFoundException("User Not Found with username: " + username);
				}else {
					List<String> permissionsByRole = permissionRepository.getPermissionsByRole(userDto.getRole().getRoleId());
					return UserDetails.build(userDto, permissionsByRole);

				}
	}

	/**
	 * Fetching the User from Database using username
	 *
	 * @param username
	 * @return UserDto
	 */
	public UserDto fetchByUsername(String username) {
		UserDto userDto = new UserDto();
		if(userRepository.findByUserDomainId(username).isPresent()){
			User user = userRepository.findByUserDomainId(username).orElseThrow(() -> new NotFoundException("User Not Found"));
			BeanUtils.copyProperties(user,userDto);
		}
		return userDto;
	}


}
