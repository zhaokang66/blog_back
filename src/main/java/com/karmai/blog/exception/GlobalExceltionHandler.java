package com.karmai.blog.exception;

import com.karmai.blog.entity.mysql.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.karmai.blog.enums.StatusCodeEnum.SYSTEM_ERROR;
import static com.karmai.blog.enums.StatusCodeEnum.VALID_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceltionHandler {

    /**
     * 处理服务异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = BizException.class)
    public Result<?> errorHandler(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 运行时异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result<?> handler(RuntimeException e) {
        log.error("运行时异常:--------------" + e.getMessage());
        return Result.fail(e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> errorHandler(MethodArgumentNotValidException e) {
        String msg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.fail(VALID_ERROR.getCode(), "参数校验失败：" + (Objects.requireNonNull(msg).endsWith(",") ? msg.substring(0,msg.length() - 2) : msg));
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception e) {
        e.printStackTrace();
        return Result.fail(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getDesc());
    }


}
