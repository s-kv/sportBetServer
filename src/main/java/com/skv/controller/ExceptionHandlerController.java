package com.skv.controller;

import com.skv.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController /*extends ResponseEntityExceptionHandler*/ {
    public static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(CustomErrorType.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ResponseBody
    public String requestHandlingNoHandlerFound(CustomErrorType ex) {
        return ex.getErrorMessage();
    }

//    @ExceptionHandler(CustomErrorType.class)
//    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
}
