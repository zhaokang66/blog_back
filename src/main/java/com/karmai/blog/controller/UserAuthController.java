package com.karmai.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @PostMapping("searchUser")
    public Result<List<SysUser>> searchUser(@RequestBody Map<String,Object> param) {
        if (param.containsKey("keyword")) {
            String keyword = param.get("keyword").toString();
            List<SysUser> userList = sysUserService.list(new QueryWrapper<SysUser>().like("username",keyword).or().like("nickName","keyword"));
            return Result.ok(userList);
        }
        return Result.fail("请传入keyword字段");
    }

}
