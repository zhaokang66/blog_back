package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.UserService;
import com.karmai.blog.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 12:58
 */
@RestController
@RequestMapping("test")
public class  UserAuthController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public Result<List<SysUser>> getUserList() {
        List<SysUser> sysUserList = userService.list();
        return Result.ok(sysUserList);
    }
    @RequestMapping("/user/login")
    public Result<String> login() {
        String token = JwtUtil.genToken("java1234");
        return Result.ok(token);
    }
}
