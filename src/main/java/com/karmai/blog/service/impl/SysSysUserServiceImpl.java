package com.karmai.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.exception.BizException;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author karmai
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-15 23:57:16
*/
@Service
public class SysSysUserServiceImpl extends ServiceImpl<UserMapper, SysUser>
    implements SysUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public SysUser getByUserName(String userName) {
        return getOne(new QueryWrapper<SysUser>().eq("username",userName));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(SysUser user) {
        //检验用户名是否已经注册
        if (checkUser(user)) {
            throw new BizException("用户名已注册!");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insert(user);
    }

    private Boolean checkUser(SysUser user) {
        SysUser user1 = getByUserName(user.getUsername());
        return Objects.nonNull(user1);
    }

    public List<GrantedAuthority> getUserAuthortity(int userId) {
        return new ArrayList<>();
    }
}




