package com.tacs.backend.controller;

import com.mongodb.MongoException;
import com.tacs.backend.dto.ExceptionResponse;
import com.tacs.backend.exception.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserException.class, EventStatusException.class})
    public final ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MongoException.class})
    public final ResponseEntity<Object> handleTimeoutException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public final ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ EntityNotFoundException.class})
    public final ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserIsNotOwnerException.class})
    public final ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({RequestNotAllowException.class})
    public final ResponseEntity<Object> handleTooManyRequest(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.TOO_MANY_REQUESTS);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionResponse exception = new ExceptionResponse();
        String errors = "";
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors = errors.concat(error.getField() + ": " + error.getDefaultMessage() + ". ");
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors = errors.concat(error.getObjectName() + ": " + error.getDefaultMessage() + ". ");
        }
        exception.setTimestamp(new Date());
        exception.setMessage(errors);

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse();
        exception.setTimestamp(new Date());
        exception.setMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
