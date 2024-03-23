package com.archipio.userservice.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.archipio.userservice.dto.ErrorDto;
import com.archipio.userservice.exception.BadPasswordException;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.exception.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionCatcher {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      HttpServletRequest request, MethodArgumentNotValidException e) {
    var errors =
        e.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(" "))));

    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.validation-error", null, RequestContextUtils.getLocale(request)))
                .errors(errors)
                .build());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorDto> handleNoHandlerFoundException(
      HttpServletRequest request, NoHandlerFoundException e) {
    return ResponseEntity.status(NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.endpoint-not-found",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(
      HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.method-not-supported",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpServletRequest request, HttpMessageNotReadableException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.invalid-json-format",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(
      HttpServletRequest request, MissingServletRequestParameterException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.missing-request-parameter",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handleUsernameAlreadyExistsException(
      HttpServletRequest request, UsernameAlreadyExistsException e) {
    return ResponseEntity.status(CONFLICT)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.username-already-exists",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handleEmailAlreadyExistsException(
      HttpServletRequest request, EmailAlreadyExistsException e) {
    return ResponseEntity.status(CONFLICT)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.email-already-exists",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDto> handleUserNotFoundException(
      HttpServletRequest request, UserNotFoundException e) {
    return ResponseEntity.status(NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.user-not-found", null, RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(BadPasswordException.class)
  public ResponseEntity<ErrorDto> handleBadPasswordException(
      HttpServletRequest request, BadPasswordException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.bad-password", null, RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(HttpServletRequest request, Exception e) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.internal-server-error",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }
}
