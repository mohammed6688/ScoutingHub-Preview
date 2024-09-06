package com.krnal.products.scoutinghub.aspect;

import com.krnal.products.scoutinghub.types.ServiceResult;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WsExceptionHandler {
    Logger logger = LoggerFactory.getLogger(WsExceptionHandler.class);
    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<?> handleValidationException(final Exception internalServerException) {
        logger.error("Request: " + request.getRequestURI(), internalServerException);

        ServiceResult serviceResult = new ServiceResult(500, "FAILED", internalServerException.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(serviceResult);
    }

}
