package com.qiudaozhang.springsecuritylearn.controller;

import com.qiudaozhang.springsecuritylearn.commom.ServerResponse;
import com.qiudaozhang.springsecuritylearn.req.PwdChangeReq;
import com.qiudaozhang.springsecuritylearn.req.UserReq;
import com.qiudaozhang.springsecuritylearn.service.UserService;
import com.qiudaozhang.springsecuritylearn.vo.UserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 邱道长
 * 2020/12/24
 */
@RestController
@RequestMapping("manage")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("user")
    public void createUser(@RequestBody UserReq req) {
        userService.createUser(req);
    }

    @GetMapping("user/{username}")
    public ServerResponse<UserVo> findUser(@PathVariable("username") String username) {
        return userService.find(username);
    }

    @PutMapping("user/changepassword")
    public ServerResponse changePassword(@RequestBody PwdChangeReq req) {
        return userService.changePassword(req);
    }

    @DeleteMapping("user/{username}")
    public ServerResponse  deleteUser(@PathVariable("username") String username) {
        return userService.delete(username);
    }
 }
