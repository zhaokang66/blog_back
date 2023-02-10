package com.karmai.blog.controller;

import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysMenu;
import com.karmai.blog.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author zhaokang03
 * @Date 2023/2/10 15:16
 */
@RestController
public class MenuController {
    @Autowired
    SysMenuService sysMenuService;


    /**
     *查看菜单列表
     * todo 这里需考虑查询条件
     */

    @GetMapping("/getMenuList")
    public Result<List<SysMenu>> getMenuList() {
        return Result.ok(sysMenuService.listMenu());
    }

    /**
     * 新增、修改菜单
     */
    @PostMapping("/insertOrUpdateMenu")
    Result<?> insertOrUpdateMenu(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.saveOrUpdate(sysMenu);
        return Result.ok();
    }

    @DeleteMapping("/deleteMenu/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId){
        sysMenuService.deleteMenu(menuId);
        return Result.ok();
    }
}
