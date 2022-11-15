package com.karmai.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 12:58
 */
@RestController
public class UserAuthController {
    @GetMapping("/users/list")
    public String sendCode() {
        return "aaa";
    }
}
