package com.karmai.blog.handler;

import cn.hutool.jwt.JWTException;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.service.impl.SysSysUserServiceImpl;
import com.karmai.blog.service.impl.UserDetailsServiceImpl;
import com.karmai.blog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JwtAuthnticationFilter extends BasicAuthenticationFilter {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
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
        try{
            String userName = JwtUtils.verify(token);
            //todo 此处查找用户信息应该从redis中取
            SysUser user = sysUserService.getByUserName(userName);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,null,userDetailsService.getUserAuthortity(user.getId()));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            chain.doFilter(request,response);
        }
        catch (SignatureVerificationException e) {
            log.error("token不一致!",e);
            throw new JWTException("token不一致!");
        }
        catch (TokenExpiredException e) {
            log.error("token已过期!",e);
            throw new JWTException("token已过期!");
        }catch (AlgorithmMismatchException e) {
            log.error("token算法不匹配!",e);
            throw new JWTException("token算法不匹配!");
        }
        catch (InvalidClaimException e) {
            log.error("失效的payload!",e);
            throw new JWTException("失效的payload!");
        }catch (Exception e) {
            log.error("token验证失败!",e);
            throw new JWTException("token验证失败!");
        }
    }
}
