package com.nibado.example.springaop.config;

import com.nibado.example.springaop.aspects.ForbiddenException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers {
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handle(final ServletRequestBindingException ex) {
        log.error("Missing parameter", ex);
        return new ErrorResponse("MISSING_PARAMETER", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handle(final IllegalArgumentException ex) {
        log.error("Missing parameter", ex);
        return new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN    )
    @ResponseBody
    public ErrorResponse handle(final ForbiddenException ex) {
        log.error("Missing parameter", ex);
        return new ErrorResponse("FORBIDDEN_ACTION", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResponse handle(final Throwable ex) {
        log.error("Unexpected error", ex);
        return new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected internal server error occured");
    }

    @Data
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
