package com.karmai.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.entity.SysMenu;
import com.karmai.blog.entity.SysRoleMenu;
import com.karmai.blog.service.SysRoleMenuService;
import com.karmai.blog.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author karmai
* @description 针对表【sys_role_menu】的数据库操作Service实现
* @createDate 2022-11-20 02:02:58
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService{
}




