package com.karmai.blog.mapper.mysql;

import com.karmai.blog.entity.mysql.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author karmai
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-11-15 23:57:16
* @Entity com.karmai.blog.entity.mysql.SysUser
*/
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

}




