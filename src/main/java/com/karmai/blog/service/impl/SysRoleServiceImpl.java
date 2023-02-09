package com.karmai.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.entity.mysql.SysRole;
import com.karmai.blog.service.SysRoleService;
import com.karmai.blog.mapper.mysql.SysRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author karmai
* @description 针对表【sys_role】的数据库操作Service实现
* @createDate 2022-11-20 02:02:58
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

}




