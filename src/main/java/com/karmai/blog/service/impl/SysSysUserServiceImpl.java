package com.karmai.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.dto.EmailDto;
import com.karmai.blog.dto.VaildEmailDto;
import com.karmai.blog.entity.SysMenu;
import com.karmai.blog.entity.SysRole;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.exception.BizException;
import com.karmai.blog.mapper.SysMenuMapper;
import com.karmai.blog.mapper.SysRoleMapper;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.mapper.SysUserMapper;
import com.karmai.blog.utils.CommonUtils;
import com.karmai.blog.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author karmai
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-15 23:57:16
*/
@Service
public class SysSysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RedisUtils redisUtils;
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
        if (StringUtils.isEmpty(user.getNickName())) {
            user.setNickName("用户" + IdWorker.getId());
        }
        sysUserMapper.insert(user);
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        StringBuilder authority=new StringBuilder(); // 根据用户id获取所有的角色
         List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole> ().inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + userId));
         if(roleList.size()>0){
             String roleCodeStrs = roleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
             authority.append(roleCodeStrs); }
         // 遍历角色，获取所有菜单权限
         Set<String> menuCodeSet=new HashSet<String>();
         for(SysRole sysRole:roleList){
             List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
             for(SysMenu sysMenu:sysMenuList){
                 String perms=sysMenu.getPerms();
                 if(StringUtils.isNotEmpty(perms)){
                     menuCodeSet.add(perms);
                 }
             } }
         if(menuCodeSet.size()>0){
             authority.append(",");
             String menuCodeStrs = String.join(",", menuCodeSet);
             authority.append(menuCodeStrs);
         }
         System.out.println("authority:"+authority.toString());
         return authority.toString();
    }

    @Override
    public void sendEmailCode(String emailName) {
        // 验证邮箱格式是否正确
        if (!CommonUtils.verifyMailFormat(emailName)) {
            throw new BizException("邮箱格式不正确！");
        }
        // 生成6位随机数
        String code = CommonUtils.genRandomCode();
        // 发送邮件
        EmailDto emailDTO = new EmailDto();
        emailDTO.setTo(emailName);
        emailDTO.setSubject("邮箱验证码");
        emailDTO.setText(code);
        rabbitTemplate.convertAndSend("mainExchange","mainRouterKey",emailDTO,new CorrelationData(System.currentTimeMillis() + "$" + UUID.randomUUID()));
        // 验证码存入redis设置缓存30分钟 根据邮箱地址设置key
        redisUtils.set("email:code:" + emailName,code,30 * 60);
    }

    @Override
    public void validEmail(VaildEmailDto data) {
        Object redisCode = redisUtils.get("email:code:" + data.getEmailName());
        if (ObjectUtils.isEmpty(redisCode)) {
            throw new BizException("验证码已过期!");
        }
        if (!data.getCode().equals(redisCode.toString())) {
            throw new BizException("验证码无效，请重新输入!");
        }
        // TODO: 2023/2/24  修改用户邮箱状态
        // 删除redis缓存
        redisUtils.del("email:code:" + data.getEmailName());

    }

    @Override
    public List<SysUser> getUserByPage(int page, int pageSize) {
        Page<SysUser> pageEntity = new Page<>(page,pageSize);
//        sysUserMapper.selectPage(pageEntity,new QueryWrapper<SysUser>().);
        return null;
    }

    private Boolean checkUser(SysUser user) {
        SysUser user1 = getByUserName(user.getUsername());
        return Objects.nonNull(user1);
    }
}




