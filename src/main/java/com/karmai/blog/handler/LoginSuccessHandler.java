package com.karmai.blog.handler;

import cn.hutool.json.JSONUtil;
import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.UserInfo;
import com.karmai.blog.utils.JwtUtil;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 13:09
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        String userId = "31D5B52C-9743-4144-9F4D-AABC0EB60FA7";
        Map<String,Object> loginData = new HashMap<>();
        loginData.put("token",JwtUtil.genToken(userId));
        UserInfo userInfo = new UserInfo();
        userInfo.setName("测试用户");
        loginData.put("userInfo",userInfo);
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(Result.ok(loginData)));
    }


}
