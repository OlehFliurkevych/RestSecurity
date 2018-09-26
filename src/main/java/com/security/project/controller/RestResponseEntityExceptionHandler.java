package com.security.project.controller;

import com.security.project.dto.RestMessageDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(value
//            = { Exception.class })
//    protected ResponseEntity<Object> handleConflict(
//            Exception ex, WebRequest request) {
//        return handleExceptionInternal(ex, RestMessageDTO
//                        .createFailureMessage(ex.getMessage()),
//                new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
    public static final String DEFAULT_ERROR_VIEW = "error-404";

    @ExceptionHandler(value = Exception.class)
    public String defaultrrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return DEFAULT_ERROR_VIEW;
    }
}
