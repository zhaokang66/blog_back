package com.karmai.blog.utils;

import com.karmai.blog.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 14:43
 */
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
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int s = random.nextInt(3);
            if (s == 0) {
                //生成数字
                int num = random.nextInt(10);
                code.append(num);
            }
            if (s == 1) {
                //生成小写字母
                int x = random.nextInt(26) + 97;
                char c = (char) x;
                code.append(c);
            }
            if (s == 2) {
                //生成大写字母
                int d = random.nextInt(26) + 65;
                char c = (char) d;
                code.append(c);
            }
        }
        return code.toString();
    }

    public static boolean vaildEmailCode(String code,String emailName) {
//        Object redisEmailCode = RedisUtils.get("email:code:" + emailName);
//        if (!ObjectUtils.isEmpty(redisEmailCode)) {
//            return code.equals(redisEmailCode.toString());
//        }
        return false;
    }

}
