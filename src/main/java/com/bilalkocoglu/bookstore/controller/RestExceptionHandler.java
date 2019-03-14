package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@PropertySource("classpath:message.properties")
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Invalid Parameter !");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse
                                .builder()
                                .message(typeMismatchMessage)
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Not Valid Parameter !");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse
                                .builder()
                                .message(notValidMessage)
                                .build()
                );
    }

    @Value("${exception.typeMismatch.message}")
    private String typeMismatchMessage;

    @Value("${exception.notvalidargs.message}")
    private String notValidMessage;
}
