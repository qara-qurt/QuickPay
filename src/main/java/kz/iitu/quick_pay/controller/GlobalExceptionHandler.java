package kz.iitu.quick_pay.controller;


import jakarta.servlet.http.HttpServletRequest;
import kz.iitu.quick_pay.exception.organization.OrganizationAlreadyExistException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.user.UserAlreadyExistsException;
import kz.iitu.quick_pay.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handling Valid errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // get only messages from errors
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        // response message
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation Error",
                "messages", messages
        );

        return ResponseEntity.badRequest().body(response);
    }

    // Handling UserAlreadyExistsException errors
    @ExceptionHandler({UserAlreadyExistsException.class, OrganizationAlreadyExistException.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(RuntimeException ex, HttpServletRequest request) {

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "messages", List.of(ex.getMessage()),
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handling UserNotFound errors
    @ExceptionHandler({UserNotFoundException.class, OrganizationNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(RuntimeException ex, HttpServletRequest request) {

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "messages", List.of(ex.getMessage()),
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handling other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, HttpServletRequest request) {
        // Forming response
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "messages", List.of(ex.getMessage()),
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

