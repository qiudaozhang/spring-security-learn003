package com.qiudaozhang.springsecuritylearn.service.impl;

import com.qiudaozhang.springsecuritylearn.commom.ServerResponse;
import com.qiudaozhang.springsecuritylearn.req.PwdChangeReq;
import com.qiudaozhang.springsecuritylearn.req.UserReq;
import com.qiudaozhang.springsecuritylearn.service.UserService;
import com.qiudaozhang.springsecuritylearn.vo.UserVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 邱道长
 * 2020/12/24
 */
@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UserDetailsManager udm;

    @Override
    public void createUser(UserReq req) {
        UserDetails u = User.withUsername(req.getUsername())
                .password(req.getPassword())
                .authorities("read")
                .build();
        udm.createUser(u);
    }

    @Override
    public ServerResponse<UserVo> find(String username) {

        boolean b = udm.userExists(username);
        if(b) {
            UserDetails u = udm.loadUserByUsername(username);
            UserVo.UserVoBuilder builder = UserVo.builder();

            builder.username(u.getUsername())
                    .password(u.getPassword());


            List<String> authorities = u.getAuthorities().stream().map(c -> c.getAuthority()).collect(Collectors.toList());
            builder.authorities(authorities);
            return ServerResponse.success(builder.build());
        } else {
            return ServerResponse.error("用户不存在！");
        }
    }

    @Override
    public ServerResponse changePassword(PwdChangeReq req) {
        try {
            udm.changePassword(req.getOldPassword(),req.getNewPassword());
            return ServerResponse.success();
        } catch (Exception e) {
            return ServerResponse.error("error");
        }

    }

    @Override
    public ServerResponse delete(String username) {
        boolean b = udm.userExists(username);
        if(b) {
            udm.deleteUser(username);
            return ServerResponse.success();
        } else {
            return ServerResponse.error("用户不存在！");
        }

    }
}
