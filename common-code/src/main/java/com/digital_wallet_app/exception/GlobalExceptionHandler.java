package com.digital_wallet_app.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)

    public ResponseEntity<Map<String, String>> handle(UserNotFoundException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("message", ex.getMessage());

        return ResponseEntity.status(404).body(error);
    }
}
