package com.karmai.blog.entity.mysql;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /*** 创建日期 */
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_on",fill = FieldFill.INSERT)
    private Date created_on;

    /*** 更新日期 */
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "lastChanged_on",fill = FieldFill.INSERT_UPDATE)
    private Date lastChanged_on;

    /*** 创建人 */
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    private Date created_by;

    /*** 最后更新人 */
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "lastChanged_by",fill = FieldFill.INSERT_UPDATE)
    private Date lastChanged_by;

    /*** 备注 */
    @TableField(value = "remark")
    private String remark;
}
