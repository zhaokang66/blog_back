package com.karmai.blog.service;

import com.karmai.blog.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author karmai
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-15 23:57:16
*/
public interface UserService extends IService<SysUser> {

    SysUser getByUserName(String userName);
}