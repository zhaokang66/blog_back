package com.karmai.blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author zhaokang03
 * @Date 2023/2/16 15:11
 */
@Data
public class EmailDto implements Serializable {
    private String to;
    private String subject;
    private String text;
}
