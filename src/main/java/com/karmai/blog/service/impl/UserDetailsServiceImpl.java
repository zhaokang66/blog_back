package com.karmai.blog.service.impl;

import com.karmai.blog.entity.SysUser;
import com.karmai.blog.enums.StatusCodeEnum;
import com.karmai.blog.exception.UserCountLockException;
import com.karmai.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUser sysUser = userService.getByUserName(userName);
        if (sysUser == null) {
            throw new UsernameNotFoundException(StatusCodeEnum.USERNAME_NOT_EXIST.getDesc());
        }else if ("1".equals(sysUser.getStatus())) {
            throw new UserCountLockException("该用户已被停用");
        }
        return new User(sysUser.getUsername(),sysUser.getPassword(),getUserAuthority());
    }

    private List<GrantedAuthority> getUserAuthority() {
        return new ArrayList<>();
    }
}
