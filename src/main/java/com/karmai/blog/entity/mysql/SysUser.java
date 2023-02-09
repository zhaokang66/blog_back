package com.karmai.blog.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karmai.blog.annotation.PhoneValid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 
 * @TableName user
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_user")
@Data
public class SysUser extends BaseEntity implements UserDetails {
    @TableId
    private Integer id;

    /**
     * 
     */
    @NotBlank(message = "用户名不能为空")
    @TableField(value = "username")
    private String username;

    /**
     * 所属角色
     */
    @TableField(exist = false)
    private String roles;

    /**
     *
     */
    @TableField(value = "nickName")
    private String nickName;

    /**
     * 
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @TableField(value = "email")
    private String email;

    /**
     * 
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 
     */
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "login_time")
    private Date loginTime;

    /**
     * 
     */
    @PhoneValid
    @TableField(value = "phoneNumber")
    private String phoneNumber;
    /**
     * 
     */
    @Size(min = 6, message = "密码不能少于6位")
    @NotBlank(message = "密码不能为空")
    @TableField(value = "password")
    private String password;

    /**
     * 确认新密码
     */
    @TableField(exist = false)
    private String newPassword;
    /**
     * 旧密码（前端传来的）
     */
    @TableField(exist = false)
    private String oldPassword;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}