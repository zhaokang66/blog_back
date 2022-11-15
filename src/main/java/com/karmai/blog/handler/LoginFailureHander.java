package com.karmai.blog.handler;

import cn.hutool.json.JSONUtil;
import com.karmai.blog.entity.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 15:26
 */
@Component
public class LoginFailureHander implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        String message = e.getMessage();
        if (e instanceof BadCredentialsException) {
            message = "用户名或者密码错误！";
        }
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(Result.fail(message)));
    }
}
