package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<List<SysUser>> getUserList() {
        List<SysUser> sysUserList = sysUserService.list();
        return Result.ok(sysUserList);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Result<SysUser> register(@Valid  @RequestBody SysUser user) {
        sysUserService.register(user);
        return Result.ok();
    }


}
