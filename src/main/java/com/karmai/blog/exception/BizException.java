package com.karmai.blog.exception;

import com.karmai.blog.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.karmai.blog.enums.StatusCodeEnum.FAIL;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 14:08
 */
@Getter
@AllArgsConstructor
public class BizException extends  RuntimeException{
    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }
}
