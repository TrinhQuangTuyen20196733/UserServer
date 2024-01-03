package com.example.UserService.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex) {
        Map<String, String> errors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        String errorMsg = errors.entrySet()
                .stream()
                .map(entry -> "Exception: " + entry.getKey() + " | Reason: " + entry.getValue())
                .collect(Collectors.joining("\n"));

        return errorMsg;

    }

    @ExceptionHandler({EmailNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setDescription(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }
    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setDescription(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }

    @ExceptionHandler({RefreshTokenFailedException.class})
    public ResponseEntity<ErrorResponse> handleRefreshTokenFailedException(RefreshTokenFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setDescription(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }

    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(4000);
        errorResponse.setDescription(ex.getMessage());
        return ResponseEntity.status(4000).body(errorResponse);

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> Exception(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(500);
        errorResponse.setDescription(e.getMessage());
        return ResponseEntity.status(500).body(errorResponse);

    }

}
