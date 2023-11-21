package com.stg.b2b.security.jwt;

import com.stg.b2b.exception.ForbiddenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;



/**
 * JWT Token related operations.
 *
 */
@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity.minutes}")
    private long validityMins;

    /**
     * This method calls doGenerateToken() method
     * for fetching the token
     *
     * @param userName
     * @param claims
     * @return JWT token
     */

    public String generateToken(String userName, Map<String, Object> claims) {
        return doGenereateToken(userName, claims);
    }

    /**
     * This method is used to generate token
     *
     * @param subject
     * @param claims
     * @return JWT token
     */

    private String doGenereateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (validityMins * 1000 * 60)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * this method retrieves all the claims from the JWT token
     * @param token
     * @return claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * This method resolves the retrieved claims from token
     *
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    /**
     * This method retrieves username from token if exists
     *
     * @param token
     * @return username
     */
    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);

    }

    /**
     * This method retrieves expiration date from token
     *
     * @param token
     * @return expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * This method checks whether the token is expired
     *
     * @param token
     * @return true / false
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * This method checks whether the token is valid
     *
     * @param token
     * @param userName
     * @return true / false
     */
    public Boolean validateToken(String token, String userName) {
        try {
            final String tokenUserName = getUserNameFromToken(token);
            return (tokenUserName.equals(userName) && !isTokenExpired(token));

        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
			throw new ForbiddenException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            throw new ForbiddenException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
            throw new ForbiddenException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
            throw new ForbiddenException("JWT claims string is empty.");
        }
    }


}
