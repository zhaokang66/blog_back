package com.karmai.blog.service;

import com.karmai.blog.dto.VaildEmailDto;
import com.karmai.blog.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author karmai
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-15 23:57:16
*/
public interface SysUserService extends IService<SysUser> {

    SysUser getByUserName(String userName);

    void register(SysUser user);

    String getUserAuthorityInfo(Long userId);

    void sendEmailCode(String emailName);

    void validEmail(VaildEmailDto data);

    List<SysUser> getUserByPage(int page, int pageSize);
}
