package com.karmai.blog.exception;

import org.springframework.security.core.AuthenticationException;

public class UserCountLockException  extends AuthenticationException {

    public UserCountLockException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserCountLockException(String msg) {
        super(msg);
    }
}
