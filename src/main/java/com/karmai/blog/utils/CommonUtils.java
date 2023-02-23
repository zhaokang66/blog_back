package com.karmai.blog.utils;

import com.karmai.blog.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 14:43
 */
@Slf4j
public class CommonUtils {

    @Autowired
    RedisUtils redisUtils;
    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static SysUser getLoginUser() {
        return (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean verifyMailFormat(String name) {
        Pattern partern = Pattern.compile("[a-zA-Z0-9]+[\\.]{0,1}[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
        return partern.matcher(name).matches();
    }

    public static String genRandomCode() {
        return String.valueOf((int)(((Math.random() * 9 + 1) * 100000)));
    }

    /**
     * 读取邮件模板
     * 替换模板中的信息
     *
     * @param title 内容
     * @return
     */
    public static String buildContent(String title) throws IOException {
        //加载邮件html模板
        Resource resource = new ClassPathResource("mailtemplate.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), title);
    }

    public static boolean vaildEmailCode(String code,String emailName) {
//        Object redisEmailCode = RedisUtils.get("email:code:" + emailName);
//        if (!ObjectUtils.isEmpty(redisEmailCode)) {
//            return code.equals(redisEmailCode.toString());
//        }
        return false;
    }

}
