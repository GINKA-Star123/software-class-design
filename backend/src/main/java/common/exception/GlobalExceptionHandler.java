package com.example.storyworkshop.common.exception;

import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.common.result.ResultCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException exception) {
        return Result.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return Result.error(ResultCode.VALIDATION_ERROR, buildBindingMessage(exception.getBindingResult()));
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException exception) {
        return Result.error(ResultCode.VALIDATION_ERROR, buildBindingMessage(exception.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = ResultCode.VALIDATION_ERROR.getMessage();
        }

        return Result.error(ResultCode.VALIDATION_ERROR, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        String message = "缺少必要请求参数：" + exception.getParameterName();
        return Result.error(ResultCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return Result.error(ResultCode.BAD_REQUEST, "请求体格式错误，请检查 JSON 格式");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        String message = "当前接口不支持 " + exception.getMethod() + " 请求";
        return Result.error(ResultCode.METHOD_NOT_ALLOWED, message);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception exception) {
        return Result.error(ResultCode.INTERNAL_ERROR);
    }

    private String buildBindingMessage(BindingResult bindingResult) {
        if (bindingResult == null || !bindingResult.hasErrors()) {
            return ResultCode.VALIDATION_ERROR.getMessage();
        }

        return bindingResult.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + Optional.ofNullable(error.getDefaultMessage()).orElse("参数错误"))
                .distinct()
                .collect(Collectors.joining("; "));
    }
}