package com.stg.b2b.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stg.b2b.exception.ForbiddenException;
import com.stg.b2b.exception.PaymentRequiredException;
import com.stg.b2b.repository.UserRepository;
import com.stg.b2b.security.jwt.JwtAuthenticationResponse;
import com.stg.b2b.security.jwt.JwtTokenUtil;
import com.stg.b2b.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil tokenProvider;

    @Autowired
    UserDetailsService userDetailsServiceImpl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebRequest webRequest;


    /**
     * In this method we are authenticating and
     * generating a token for the valid authenticated user
     *
     * @param loginRequest
     * @return StandardResponse which contains token related data
     */
    @PostMapping("/generate-token")
    public ResponseEntity<StandardResponse> authenticateUser(@RequestBody SignInDto loginRequest) {
        String jwt = null;
        ObjectMapper oMapper = new ObjectMapper();
        String loginUserName = loginRequest.getUsername();

        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new StandardResponse(new Date(), "Please enter username and password", webRequest.getDescription(false), HttpStatus.BAD_REQUEST.toString(), null));
        }

        try {
             authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginUserName,
                            loginRequest.getPassword()));
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginUserName);
            Map<String, Object> claims = oMapper.convertValue(userDetails, Map.class);
            System.out.println("**********************");
            System.out.println(userDetails.getAuthorities());
            jwt = tokenProvider.generateToken(loginUserName, claims);
        } catch (BadCredentialsException e) {
            throw new PaymentRequiredException("Please check your credentials");
        } catch (AuthenticationException e) {
            throw new ForbiddenException(e.getMessage());
        } catch (PaymentRequiredException e){
            throw new PaymentRequiredException("Please check your credentials");
        }

        return ResponseEntity.ok(new StandardResponse(new Date(), "OK", webRequest.getDescription(false), HttpStatus.OK.toString(), (new JwtAuthenticationResponse(jwt))));
    }
}
