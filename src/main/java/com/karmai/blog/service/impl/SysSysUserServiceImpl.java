package com.karmai.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author karmai
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-15 23:57:16
*/
@Service
public class SysSysUserServiceImpl extends ServiceImpl<UserMapper, SysUser>
    implements SysUserService {

    @Override
    public SysUser getByUserName(String userName) {
        return getOne(new QueryWrapper<SysUser>().eq("username",userName));
    }

    public List<GrantedAuthority> getUserAuthortity(int userId) {
        return new ArrayList<>();
    }
}




