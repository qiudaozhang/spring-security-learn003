package com.qiudaozhang.springsecuritylearn.service;

import com.qiudaozhang.springsecuritylearn.commom.ServerResponse;
import com.qiudaozhang.springsecuritylearn.req.PwdChangeReq;
import com.qiudaozhang.springsecuritylearn.req.UserReq;
import com.qiudaozhang.springsecuritylearn.vo.UserVo;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author 邱道长
 * 2020/12/24
 */
public interface UserService {
    void createUser(UserReq req);

    ServerResponse<UserVo> find(String username);

    ServerResponse changePassword( PwdChangeReq req);

    ServerResponse  delete(String username);
}
