package com.task.task.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.task.task.codes.ResponseCodes.*;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, RecordNotFoundException.class})
    public ResponseEntity handleEmptyResultDataAccess(final RuntimeException exc, final WebRequest request) {
        final Response body = new Response(String.format(DO_NOT_EXIST.getValue(), request.getParameter("primaryKey")));
        return handleExceptionInternal(exc, body,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxUploadSizeException(final RuntimeException exc, final WebRequest request) {
        final Response body = new Response(FILE_IS_TOO_BIG.getValue());
        return handleExceptionInternal(exc, body, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(final RuntimeException exc, final WebRequest request) {
        final Response body = new Response(WRONG_RECORDS_FORMAT.getValue());
        return handleExceptionInternal(exc, body, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(final RuntimeException exc, final WebRequest request) {
        final Response body = new Response(PROBLEM_WHILE_SAVING.getValue());
        return handleExceptionInternal(exc, body, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }
}
