package com.stg.b2b.security.jwt;


import com.stg.b2b.exception.ForbiddenException;
import com.stg.b2b.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * JWT Request Filter is responsible for intercepting incoming requests
 * and validating the JWT included in the request.
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * The doFilterInternal() method extracts the JWT token from the request, validates it,
     * and if valid, creates an authenticated Authentication object representing the user
     * If the token is invalid, an error response is sent,
     * and if an exception occurs during authentication, an appropriate error response is sent
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUserNameFromToken(jwtToken);

            } catch (IllegalArgumentException e) {
                throw new ForbiddenException("User Not Allowed");
            } catch (ExpiredJwtException e) {
                throw new UnauthorizedException("User is Unauthorized");
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, username))) {

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            username, null, null);
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        chain.doFilter(request, response);
    }

}
