package com.lms.www.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {

        String msg = ex.getMessage();

        if (msg != null && msg.contains("Account locked")) {
            return ResponseEntity
                    .status(HttpStatus.LOCKED)   // 423
                    .body(new ApiError(msg));
        }

        if (msg != null && msg.contains("Password expired")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError(msg));
        }

        if (msg != null && msg.contains("Invalid credentials")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError(msg));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(msg));
    }

    // 🔹 SIMPLE RESPONSE STRUCTURE
    static class ApiError {
        private final String message;

        public ApiError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
