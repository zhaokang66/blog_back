package com.karmai.blog.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

/**
 * @Author zhaokang03
 * @Date 2023/2/9 15:11
 */
@Data
@Document(indexName = "blog",useServerConfiguration = true,createIndex = false)
public class SysUser {
    @Id
    private Integer id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String username;
    private String roles;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String nickName;
    private String email;
    private String status;
    private String avatar;
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private Date loginTime;
    private String phoneNumber;
    private String password;
    private String newPassword;
}
