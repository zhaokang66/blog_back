package com.karmai.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysRole;
import com.karmai.blog.entity.SysUserRole;
import com.karmai.blog.service.SysRoleService;
import com.karmai.blog.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author zhaokang03
 * @Date 2022/12/12 15:48
 */
@RestController
@RequestMapping("admin/role")
public class UserRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 返回所有角色
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<Map<String,Object>> list() {
        Map<String,Object> res = new HashMap<>();
        List<SysRole> roleList = sysRoleService.list();
        res.put("roleList",roleList);
        return Result.ok(res);
    }

    /**
     * 用户角色授权
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "grantRole",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:role')")
    public Result grantRole(Long userId, @RequestBody Long[] roleIds) {
        // 此处应该加事务管理,默认事务失效的原因：1：类不是public2、数据库不支持事务3、异常没用抛出，被catch了4、异常类型，默认没指定rollbackFor的话，检查异常不触发回滚
        // 先删除用户角色表中userid的记录
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        List<SysUserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(i -> {
           SysUserRole indexData = new SysUserRole();
            indexData.setRoleId(i);
            indexData.setUserId(userId);
            userRoleList.add(indexData);
        });
        sysUserRoleService.saveBatch(userRoleList);
        return Result.ok();
    }
}
