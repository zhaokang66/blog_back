package com.karmai.blog.service.impl;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karmai.blog.entity.SysMenu;
import com.karmai.blog.entity.SysRoleMenu;
import com.karmai.blog.exception.BizException;
import com.karmai.blog.mapper.SysRoleMenuMapper;
import com.karmai.blog.service.SysMenuService;
import com.karmai.blog.mapper.SysMenuMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* @author karmai
* @description 针对表【sys_menu】的数据库操作Service实现
* @createDate 2022-11-20 02:02:58
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        List<SysMenu> resultMenuList=new ArrayList<>();
        for(SysMenu sysMenu:sysMenuList){
            // 寻找子节点
            for(SysMenu e:sysMenuList){
                if(e.getParentId()==sysMenu.getId()){
                    sysMenu.getChildren().add(e);
                }
            }
            // 判断父节点，添加到集合
            if(sysMenu.getParentId()==0L){
                resultMenuList.add(sysMenu);
            }
        }
        return resultMenuList;
    }

    @Override
    public List<SysMenu> listMenu() {
        // 查出菜单列表 条件 menuType=M or C
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuType, "M").
                or()
                .eq(SysMenu::getMenuType, "C")).stream().sorted(Comparator.comparing(SysMenu::getOrderNum)).collect(Collectors.toList());
        // 测试作为生产者向MQ发送消息
        rabbitTemplate.convertAndSend("testExchange","test.routerKey",sysMenuList,new CorrelationData(System.currentTimeMillis() + "$" + UUID.randomUUID()));
        return buildTreeMenu(sysMenuList);
    }

    @Override
    public void deleteMenu(Integer menuId) {
        // 菜单 -》角色 -》 用户
        // 首先判断当前菜单是否被角色关联
        Integer count1 = sysRoleMenuMapper.selectCount(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId));
        if (count1 > 0) {
            throw new BizException("当前菜单已被角色关联");
        }
        // 判断当前删除的菜单是否有子菜单
        int count2 = count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId,menuId));
        if (count2 > 0) {
            throw new BizException("请先删除当前菜单的子级菜单");
        }
        removeById(menuId);
    }
}




