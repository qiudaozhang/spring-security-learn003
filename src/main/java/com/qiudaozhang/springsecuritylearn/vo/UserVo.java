package com.qiudaozhang.springsecuritylearn.vo;

import lombok.*;

import java.util.List;

/**
 * @author 邱道长
 * 2020/12/24
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private String username;

    private String password;

    private List<String> authorities;
}
