package com.karmai.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
public class SysMenu extends BaseEntity implements Serializable {
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @TableField(value = "name")
    private String name;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 父菜单ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空")
    @TableField(value = "path")
    private String path;

    @TableField(exist = false)
    private List<SysMenu> children=new ArrayList<>();

    /**
     * 组件路径
     */
    @NotBlank(message = "组件路径不能为空")
    @TableField(value = "component")
    private String component;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @TableField(value = "menu_type")
    private String menuType;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 子级菜单
     */
    @TableField(exist = false)
    private List<SysMenu> childMenu;
}