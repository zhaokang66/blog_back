package com.karmai.blog.annotation.impl;

import com.karmai.blog.annotation.PhoneValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 11:31
 */
public class PhoneNumberConstraintValidator implements ConstraintValidator<PhoneValid, String> {

    private static final Pattern PATTERN = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$+");

    @Override
    public void initialize(PhoneValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            Matcher matcher = PATTERN.matcher(s);
            return matcher.find();
        }
        return true;
    }
}
