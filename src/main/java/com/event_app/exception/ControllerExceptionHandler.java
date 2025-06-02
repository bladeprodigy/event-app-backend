package com.event_app.exception;

import com.event_app.error.CustomError;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<CustomError> handleUncaughtError(Exception ex) {
    log.error("Uncaught exception", ex);
    CustomError customError = new CustomError(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<CustomError> handleAccessDeniedException(AccessDeniedException ex) {
    log.error("Access denied", ex);
    CustomError customError = new CustomError(
        HttpStatus.FORBIDDEN,
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(customError, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<CustomError> handleAuthenticationException(AuthenticationException ex) {
    log.warn("Authentication error", ex);
    CustomError customError = new CustomError(
        HttpStatus.UNAUTHORIZED,
        "Invalid email or password",
        LocalDateTime.now()
    );
    return new ResponseEntity<>(customError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<CustomError> handleEntityNotFoundException(EntityNotFoundException ex) {
    log.warn("Entity not found", ex);
    CustomError customError = new CustomError(
        HttpStatus.NOT_FOUND,
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityExistsException.class)
  protected ResponseEntity<CustomError> handleEntityExistsException(EntityExistsException ex) {
    log.warn("Entity already exists", ex);
    CustomError customError = new CustomError(
        HttpStatus.CONFLICT,
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(customError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<List<CustomError>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.warn("Validation error", ex);
    List<CustomError> customErrors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String errorMessage = error.getDefaultMessage();
      CustomError customError = new CustomError(
          HttpStatus.BAD_REQUEST,
          errorMessage,
          LocalDateTime.now());
      customErrors.add(customError);
    });
    return new ResponseEntity<>(customErrors, HttpStatus.BAD_REQUEST);
  }
}

