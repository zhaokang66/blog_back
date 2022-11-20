package com.karmai.blog.service;

import com.karmai.blog.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author karmai
* @description 针对表【sys_menu】的数据库操作Service
* @createDate 2022-11-20 02:02:58
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList);
}
