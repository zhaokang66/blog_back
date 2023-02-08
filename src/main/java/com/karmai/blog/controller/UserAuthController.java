package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping(value = "/list")
//    @PreAuthorize("hasRole('ROLE_common')")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<List<SysUser>> getUserList() {
        List<SysUser> sysUserList = sysUserService.list();
        return Result.ok(sysUserList);
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public Result<SysUser> register(@Valid  @RequestBody SysUser user) {
        sysUserService.register(user);
        return Result.ok();
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/updateUserPwd")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<String> updateUserPwd(@RequestBody SysUser user) {
        SysUser currentUser = sysUserService.getById(user.getId());
        if (new BCryptPasswordEncoder().matches(user.getOldPassword(),currentUser.getPassword())) {
            currentUser.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword()));
            sysUserService.updateById(currentUser);
        }else {
            return Result.fail("输入的旧密码错误！");
        }
        return Result.ok();
    }

    /**
     *  搜索用户，根据用户昵称或者用户名搜索 eslaticsearch
     */

}
