package com.karmai.blog.dto;

import lombok.Data;

/**
 * @Author zhaokang03
 * @Date 2023/2/16 15:11
 */
@Data
public class EmailDTO {
    private String to;
    private String subject;
    private String text;
}
