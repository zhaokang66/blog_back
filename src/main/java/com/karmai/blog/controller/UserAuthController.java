package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 12:58
 */
@RestController
@RequestMapping("user")
public class  UserAuthController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/list")
    public Result<List<SysUser>> getUserList() {
        List<SysUser> sysUserList = sysUserService.list();
        return Result.ok(sysUserList);
    }
}
