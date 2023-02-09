package com.karmai.blog.handler;

import cn.hutool.json.JSONUtil;
import com.karmai.blog.entity.mysql.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 15:39
 */
@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("开始注销...");
        httpServletResponse.setContentType("applcation/json");
        ServletOutputStream outStream = httpServletResponse.getOutputStream();
        outStream.write(JSONUtil.toJsonStr(Result.ok("注销成功！")).getBytes(StandardCharsets.UTF_8));
        outStream.flush();
        outStream.close();
    }
}
