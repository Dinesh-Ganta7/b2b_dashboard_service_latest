package com.stg.b2b.exception;

import com.stg.b2b.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);
    /* **** 925 - REGARDING FILE  **** */
    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardResponse> fileExceptionHandler(FileException fileException, WebRequest webRequest){
        fileException.printStackTrace();
        StandardResponse exceptionDetails = new StandardResponse(new Date(),fileException.getMessage(), webRequest.getDescription(false), "925",null);
        return new ResponseEntity<>(exceptionDetails,HttpStatus.OK);
    }

    /* **** 402 - PAYMENT_REQUIRED **** */
    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<StandardResponse> badCredentialsExceptionHandler(PaymentRequiredException badCredentialsException, WebRequest webRequest){
        badCredentialsException.printStackTrace();
        StandardResponse exceptionDetails = new StandardResponse(new Date(),badCredentialsException.getMessage(), webRequest.getDescription(false), HttpStatus.PAYMENT_REQUIRED.toString(),null);
        return new ResponseEntity<>(exceptionDetails,HttpStatus.OK);
    }

    /* **** 403 - FORBIDDEN **** */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardResponse> authenticationExceptionHandler(ForbiddenException authenticationException, WebRequest webRequest){
        authenticationException.printStackTrace();
        StandardResponse exceptionDetails = new StandardResponse(new Date(),authenticationException.getMessage(), webRequest.getDescription(false), HttpStatus.FORBIDDEN.toString(),null);
        return new ResponseEntity<>(exceptionDetails,HttpStatus.OK);
    }

    /* **** 400 - BAD_REQUEST **** */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardResponse> authenticationExceptionHandler(BadRequestException badRequestException, WebRequest webRequest){
        badRequestException.printStackTrace();
        StandardResponse exceptionDetails = new StandardResponse(new Date(),badRequestException.getMessage(), webRequest.getDescription(false), HttpStatus.BAD_REQUEST.toString(),null);
        return new ResponseEntity<>(exceptionDetails,HttpStatus.OK);
    }

    /* **** 401 - UNAUTHORIZED **** */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardResponse> unauthorizedExceptionExceptionHandler(UnauthorizedException unauthorizedException, WebRequest webRequest){
        unauthorizedException.printStackTrace();
        StandardResponse exceptionDetails = new StandardResponse(new Date(),unauthorizedException.getMessage(), webRequest.getDescription(false), HttpStatus.UNAUTHORIZED.toString(),null);
        return new ResponseEntity<>(exceptionDetails,HttpStatus.OK);
    }

    /* **** 404 - NOT_FOUND **** */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardResponse> notFoundExceptionExceptionHandler(NotFoundException notFoundException, WebRequest webRequest){
        notFoundException.printStackTrace();
        logger.error("NOT FOUND  Error: ",notFoundException);
        StandardResponse exceptionDetails = new StandardResponse(new Date(),notFoundException.getMessage(), webRequest.getDescription(false), HttpStatus.NOT_FOUND.toString(),null);
        return new ResponseEntity<>(exceptionDetails,HttpStatus.OK);
    }

    /* **** 500 - INTERNAL_SERVER_ERROR **** */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> serverExceptionHandler(Exception exception, WebRequest webRequest){
        exception.printStackTrace();
        StandardResponse errorDetails = new StandardResponse(new Date(),exception.getMessage(), webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.toString(),null);
        return new ResponseEntity<>(errorDetails,HttpStatus.OK);
    }

}
