package com.qiudaozhang.springsecuritylearn.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 邱道长
 * 2020/12/24
 */
@RestController
public class AuthenticationController {


    @GetMapping("auth/test")
    public String test() {
        System.out.println("test");
        return "ok";
    }


    @PostMapping("auth/post")
    public String post() {
        return "post";
    }

    @GetMapping("auth/authentication")
    public String authentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println(authentication);
        return "authentication";
    }



}
