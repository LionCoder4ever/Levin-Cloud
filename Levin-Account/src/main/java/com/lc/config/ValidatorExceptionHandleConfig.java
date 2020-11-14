package com.lc.config;

import com.lc.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 验证配置
 */
@Slf4j
@RestControllerAdvice
public class ValidatorExceptionHandleConfig {

    /**
     * hibernate validator 数据绑定验证异常拦截
     *
     * @param e 绑定验证异常
     * @return 错误返回消息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public CommonResult validateErrorHandler(BindException e) {
        ObjectError error =  e.getAllErrors().get(0);
        log.error("数据验证异常：{}", error.getDefaultMessage());
        return CommonResult.failed(error.getDefaultMessage());
    }

    /**
     * hibernate validator 数据绑定验证异常拦截
     *
     * @param e 绑定验证异常
     * @return 错误返回消息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult validateErrorHandler(MethodArgumentNotValidException e) {
        ObjectError error = e.getBindingResult().getAllErrors().get(0);
        log.error("数据验证异常：{}", error.getDefaultMessage());
        return CommonResult.failed(error.getDefaultMessage());
    }
}
