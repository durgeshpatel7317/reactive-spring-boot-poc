package com.example.demo.exceptions.handler;

import com.example.demo.exceptions.ProductNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class NonReactiveGlobalExceptionHandler {
    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFound ex) {
        log.error(ex.getMessage());

        return ResponseEntity.badRequest()
            .body(Map.of("error", ex.getMessage(), "status", HttpStatus.BAD_REQUEST.toString()));
    }
}
