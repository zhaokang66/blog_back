package com.karmai.blog.handler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.karmai.blog.entity.Result;
import com.karmai.blog.entity.SysMenu;
import com.karmai.blog.entity.SysRole;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.mapper.SysUserMapper;
import com.karmai.blog.service.SysMenuService;
import com.karmai.blog.service.SysRoleService;
import com.karmai.blog.service.SysUserService;
import com.karmai.blog.utils.CommonUtils;
import com.karmai.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 13:09
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        ServletOutputStream outStream = httpServletResponse.getOutputStream();
        SysUser userInfo = CommonUtils.getLoginUser();
        String userName = userInfo.getUsername();
        sysUserService.update(
                new UpdateWrapper<SysUser>()
                        .set("login_time", new Date())
                        .eq("username", userName)
        );
        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + userInfo.getId()));
        // 遍历角色，获取所有菜单权限
        Set<SysMenu> menuSet = new HashSet<SysMenu>();
        for (SysRole sysRole : roleList) {
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
            menuSet.addAll(sysMenuList);
        }
        List<SysMenu> sysMenuList = new ArrayList<>(menuSet);
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));
        List<SysMenu> menuList = sysMenuService.buildTreeMenu(sysMenuList);
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("token", JwtUtils.genToken(userName));
        loginData.put("userInfo", userInfo);
        loginData.put("menuList", menuList);
        outStream.write(JSONUtil.toJsonStr(Result.ok(loginData)).getBytes(StandardCharsets.UTF_8));
        outStream.flush();
        outStream.close();
    }

}
