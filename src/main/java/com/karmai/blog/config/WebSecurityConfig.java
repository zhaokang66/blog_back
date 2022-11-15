package com.karmai.blog.config;

import com.karmai.blog.handler.LoginFailureHander;
import com.karmai.blog.handler.LoginSuccessHandler;
import com.karmai.blog.handler.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHander loginFailureHander;
    @Autowired
    LogoutSuccessHandlerImpl logoutSuccessHandler;



    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置登录注销路径
        http.formLogin()
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHander)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler);
        // 配置路由权限信息
//        http.authorizeRequests()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
//                        fsi.setSecurityMetadataSource();
//                        fsi.setAccessDecisionManager(accessDecisionManager());
//                        return fsi;
//                    }
//                })
//                .anyRequest().permitAll()
//                .and()
//                // 关闭跨站请求防护
//                .csrf().disable().exceptionHandling()
//                // 未登录处理
//                .authenticationEntryPoint(authenticationEntryPoint)
//                // 权限不足处理
//                .accessDeniedHandler(accessDeniedHandler)
//                .and()
//                .sessionManagement()
//                .maximumSessions(20)
//                .sessionRegistry(sessionRegistry());
    }
}
