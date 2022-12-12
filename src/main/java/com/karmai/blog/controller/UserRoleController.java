package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysRole;
import com.karmai.blog.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhaokang03
 * @Date 2022/12/12 15:48
 */
@RestController
@RequestMapping("admin/role")
public class UserRoleController {

    @Autowired
    private SysRoleService sysRoleService;

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
    @RequestMapping("grantRole")
}
