package com.stg.b2b.security.jwt;

import lombok.Data;

/**
 * JWT Security related operations.
 * 
 * @author Rahul Ravindra
 *
 */
@Data
public class JwtAuthenticationResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accessToken;
	private String tokenType = "Bearer";

	/**
	 * Here we are setting the accessToken for sending the response
	 * @param accessToken
	 */
	public JwtAuthenticationResponse(String accessToken) {
		this.accessToken = accessToken;
	}


}
