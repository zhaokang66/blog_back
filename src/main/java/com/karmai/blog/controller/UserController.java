package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zhaokang03
 * @Date 2023/06/05 14:02
 */
@RestController
@RequestMapping("/admin")
public class UserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 返回所有的用户
     * @return 用户列表
     */
    @GetMapping(value = "/users")
    public Result<List<SysUser>> getAllUser() {
        List<SysUser> sysUserList = sysUserService.list();
        return Result.ok(sysUserList);
    }

    /**
     * 根据分页信息返回用户
     * @param page 当前页数
     * @param pageSize 每页数据量
     * @return 用户列表
     */
    @GetMapping(value = "/users",params = {"page","pageSize"})
    public Result<List<SysUser>> getUserByPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        List<SysUser> userList = sysUserService.getUserByPage(page,pageSize);
        return Result.ok(userList);
    }

    /**
     * 根据用户id删除用户
     * @param userId 用户id
     * @return 删除结果
     */
    @DeleteMapping(value = "/users/{userId}")
    public Result<?> deleteUser(@PathVariable Long userId) {
        return Result.ok();
    }

    /**
     * 新增用户
     * @param sysUser 用户实体
     * @return 新增结果
     */
    @PostMapping(value = "/users")
    public Result<?> insertUser(@RequestBody SysUser sysUser) {
        return Result.ok();
    }

    /**
     * 根据用户id返回用户信息
     * @param userId 用户id
     * @return 用户实体
     */
    @GetMapping(value = "/users/{userId}")
    public Result<?> getUserById(@PathVariable Long userId) {
        return Result.ok();
    }

    /**
     * 根据用户id更新用户
     * @param userId 用户id
     * @return 更新结果
     */
    @PutMapping(value = "/users/{userId}")
    public Result<?> updateUser(@PathVariable Long userId) {
        return Result.ok();
    }
}
