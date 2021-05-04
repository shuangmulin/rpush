package com.regent.rpush.scheduler.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiResult<String> validationErrorHandler(BindException ex) throws JsonProcessingException {
        return parseValidationError(ex.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResult<String> validationErrorHandler(MethodArgumentNotValidException ex) throws JsonProcessingException {
        return parseValidationError(ex.getBindingResult());
    }

    private ApiResult<String> parseValidationError(BindingResult bindingResult) throws JsonProcessingException {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> message = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            message.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(message.values());
        return ApiResult.of(StatusCode.VALIDATE_FAIL, json);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ApiResult<String> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        LOGGER.warn(ex.getMessage(), ex);
        return ApiResult.of(StatusCode.VALIDATE_FAIL, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult<String> validationErrorHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ApiResult.of(StatusCode.FAILURE, ex.getMessage());
    }
}
