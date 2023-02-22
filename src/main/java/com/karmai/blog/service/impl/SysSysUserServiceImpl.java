package com.karmai.blog.service.impl;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.dto.EmailDTO;
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
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTo(emailName);
        emailDTO.setSubject("邮箱验证码");
        String text = "<html><head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><b>您的邮箱验证码为:<b>" +  code + "</b>,验证码有效期为<span style=\"color:red;\">15分钟</span>，请尽快验证</body></html>";
        emailDTO.setText(text);
        rabbitTemplate.convertAndSend("mainExchange","mainRouterKey",emailDTO,new CorrelationData(System.currentTimeMillis() + "$" + UUID.randomUUID()));
        // 验证码存入redis设置缓存15分组 根据邮箱地址设置key
        redisUtils.set("email:code:" + emailName,code,15 * 60);
    }

    private Boolean checkUser(SysUser user) {
        SysUser user1 = getByUserName(user.getUsername());
        return Objects.nonNull(user1);
    }
}




