package com.karmai.blog.config;

import com.karmai.blog.handler.JwtAuthnticationFilter;
import com.karmai.blog.handler.LoginFailureHander;
import com.karmai.blog.handler.LoginSuccessHandler;
import com.karmai.blog.handler.LogoutSuccessHandlerImpl;
import com.karmai.blog.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class  WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/test/**",
            "/image/**",
    };
    @Bean
    JwtAuthnticationFilter jwtAuthnticationFilter() throws Exception {
        JwtAuthnticationFilter jwtAuthnticationFilter = new JwtAuthnticationFilter(authenticationManager());
        return jwtAuthnticationFilter;
    }
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHander loginFailureHander;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //无状态
        //拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()//白名单
                .anyRequest().authenticated()
        //异常处理
        //自定义过滤器配置
                .and()
                .addFilter(jwtAuthnticationFilter());



        // 配置登录注销路径
//        http.formLogin()
//                .loginProcessingUrl("/login")
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginFailureHander)
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(logoutSuccessHandler);
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
