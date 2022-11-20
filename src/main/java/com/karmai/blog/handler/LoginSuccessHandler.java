package com.karmai.blog.handler;

import cn.hutool.json.JSONUtil;
import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.mapper.SysUserMapper;
import com.karmai.blog.utils.CommonUtils;
import com.karmai.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 13:09
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        ServletOutputStream outStream = httpServletResponse.getOutputStream();
//        SysUser userInfo = ((SysUser)(authentication.getPrincipal()));
        String userName = authentication.getName();
        SysUser userInfo = new SysUser();
        Map<String,Object> loginData = new HashMap<>();
        loginData.put("token", JwtUtils.genToken(userName));
        loginData.put("userInfo", userInfo);
//        updateUserInfo();
        outStream.write(JSONUtil.toJsonStr(Result.ok(loginData)).getBytes(StandardCharsets.UTF_8));
        outStream.flush();
        outStream.close();
    }

    @Async
    public void updateUserInfo() {
        SysUser userInfo = CommonUtils.getLoginUser();
        userInfo.setLoginTime(new Date());
        sysUserMapper.updateById(userInfo);
    }

}
