package com.karmai.blog.handler;

import cn.hutool.jwt.JWTException;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.service.impl.SysSysUserServiceImpl;
import com.karmai.blog.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JwtAuthnticationFilter extends BasicAuthenticationFilter {
    @Autowired
    private SysSysUserServiceImpl sysSysUserService;

    @Autowired
    private SysUserService sysUserService;
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/image/**",
    };

    public JwtAuthnticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token) || new ArrayList<String>(Arrays.asList(URL_WHITELIST)).contains(request.getRequestURI())) {
            chain.doFilter(request,response);
            return;
        }
        //todo 此处jwt验证应优化，应该返回不同的错误信息（过期、token不存在、token验证不通过）
        String userName = JwtUtil.verify(token);
        if (userName == null) {
            throw new JWTException("token验证失败!");
        }
        SysUser user = sysUserService.getByUserName(userName);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName,null,sysSysUserService.getUserAuthortity(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);
    }


}
