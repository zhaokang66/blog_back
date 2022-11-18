package com.karmai.blog.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.karmai.blog.entity.SysUser;
import com.karmai.blog.utils.CommonUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author zhaokang03
 * @Date 2022/11/18 14:18
 */
@Component
public class DbAutoFillFiledHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        SysUser loginUser = CommonUtils.getLoginUser();
        this.strictInsertFill(metaObject, "created_on", Date.class, new Date());
        this.strictInsertFill(metaObject, "created_by", String.class,loginUser.getUsername());
        this.strictInsertFill(metaObject, "lastChanged_by", String.class,loginUser.getUsername());
        this.strictInsertFill(metaObject, "lastChanged_on", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        SysUser loginUser = CommonUtils.getLoginUser();
        this.strictInsertFill(metaObject, "lastChanged_on", Date.class, new Date());
        this.strictInsertFill(metaObject, "lastChanged_by", String.class,loginUser.getUsername());
    }
}
