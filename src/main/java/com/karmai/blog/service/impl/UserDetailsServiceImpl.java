package com.karmai.blog.service.impl;

import com.karmai.blog.entity.SysUser;
import com.karmai.blog.enums.StatusCodeEnum;
import com.karmai.blog.exception.BizException;
import com.karmai.blog.exception.UserCountLockException;
import com.karmai.blog.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(userName)) {
            throw new BizException("用户名不能为空!");
        }
        SysUser sysUser = sysUserService.getByUserName(userName);
        if (sysUser == null) {
            throw new UsernameNotFoundException(StatusCodeEnum.USERNAME_NOT_EXIST.getDesc());
        }else if ("1".equals(sysUser.getStatus())) {
            throw new UserCountLockException("该用户已被停用");
        }
        return sysUser;
    }

    public List<GrantedAuthority> getUserAuthortity(Long userId) {
        // 格式 ROLE_admin,ROLE_common,system:user:resetPwd,system:role:delete,system:user:list, system:menu:query,system:menu:list,system:menu:add,system:user:delete,system:rol e:list,system:role:menu,system:user:edit,system:user:query,system:role:edit,syst em:user:add,system:user:role,system:menu:delete,system:role:add,system:role:quer y,system:menu:edit
        String authority=sysUserService.getUserAuthorityInfo(userId);
        System.out.println("authority="+authority);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
