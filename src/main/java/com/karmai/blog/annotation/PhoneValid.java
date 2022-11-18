package com.karmai.blog.annotation;

import com.karmai.blog.annotation.impl.PhoneNumberConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 11:25
 */
    @Documented
    @Constraint(
            validatedBy = {PhoneNumberConstraintValidator.class}
    )
// 注解可以放在xx上
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PhoneValid {

        // 注解的提示信息
        String message() default "手机号码格式不正确";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
