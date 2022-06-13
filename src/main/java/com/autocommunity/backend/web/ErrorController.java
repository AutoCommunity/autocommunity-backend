package com.autocommunity.backend.web;

import com.autocommunity.backend.exception.BadRequestException;
import com.autocommunity.backend.exception.UnauthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ValidationException;
import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class ErrorController extends AbstractController {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> responseStatusException(RuntimeException e) {
        e.printStackTrace();
        if (e instanceof BadRequestException) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        else if (e instanceof UnauthenticatedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        else if (e instanceof ValidationException) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        else if (e instanceof WebExchangeBindException) {
            var exc = ((WebExchangeBindException)e);
            var errorMsg = Optional.ofNullable(exc.getFieldError())
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .orElse("Validation error");
            return ResponseEntity.badRequest().body(errorMsg);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
