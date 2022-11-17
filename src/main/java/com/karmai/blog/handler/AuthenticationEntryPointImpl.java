package com.karmai.blog.handler;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.karmai.blog.entity.Result;
import com.karmai.blog.enums.StatusCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(Result.fail(StatusCodeEnum.NO_LOGIN)));
    }
}
