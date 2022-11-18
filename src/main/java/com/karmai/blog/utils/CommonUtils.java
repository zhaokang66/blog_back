package com.karmai.blog.utils;

import com.karmai.blog.entity.SysUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 14:43
 */
public class CommonUtils {
    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static SysUser getLoginUser() {
        return (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
