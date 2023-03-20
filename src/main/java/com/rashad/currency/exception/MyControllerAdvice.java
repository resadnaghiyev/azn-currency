package com.rashad.currency.exception;

import com.rashad.currency.model.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xml.sax.SAXParseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception) {
        Map<String, List<String>> errorMap = new HashMap<>();
        String message = exception.getMessage();
        String field = message.substring(0, message.indexOf(":"));
        String error = message.substring(message.indexOf(":") + 2);
        errorMap.put(field, List.of(error));
        return new ResponseEntity<>(new CustomResponse(false, errorMap), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<?> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("method", exception.getMessage());
        return new ResponseEntity<>(new CustomResponse(false, errorMap), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SAXParseException.class)
    public ResponseEntity<?> handle(SAXParseException exception) {
        String message = "26.11.1993 - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " tarixləri arasında axtarış edə bilərsiz.";
        return new ResponseEntity<>(new CustomResponse(false, message), HttpStatus.BAD_REQUEST);
    }
}
