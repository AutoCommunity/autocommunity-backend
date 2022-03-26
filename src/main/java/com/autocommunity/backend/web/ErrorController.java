package com.autocommunity.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ErrorController extends AbstractController {
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ReplyBase> responseStatusException(ResponseStatusException e) {
        ReplyBase exceptionWrapper = new ReplyBase(e.getMessage(), e.getStatus().toString().split(" ")[1]);
        return ResponseEntity.status(e.getStatus()).body(exceptionWrapper);
    }
}
