package com.karmai.blog.config;

import com.karmai.blog.handler.*;
import com.karmai.blog.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class  WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/image/**",
    };
    @Bean
    JwtAuthnticationFilter jwtAuthnticationFilter() throws Exception {
        return new JwtAuthnticationFilter(authenticationManager());
    }
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHander loginFailureHander;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtAuthenticationEntryPointImpl jwtAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //开启跨域、csr攻击、 关闭
        http
                .cors()
                .and()
                .csrf()
                .disable()
        //登录登出
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHander)
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                //session禁用
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //无状态，启用token认证
        //拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()//白名单
                .anyRequest().authenticated()
        //异常处理
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        //自定义过滤器配置
                .and()
                .addFilter(jwtAuthnticationFilter());
    }
}
