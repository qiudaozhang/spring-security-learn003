package com.qiudaozhang.springsecuritylearn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author 邱道长
 * 2020/12/24
 */

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter    {

    @Bean
    public UserDetailsManager userDetailsManager ( ) {
        UserDetailsManager udm = new InMemoryUserDetailsManager();
        // 定义用户
        UserDetails user = User.withUsername("admin")
                .password("123")
                .authorities("read")
                .build();
        udm.createUser(user);
        return udm;
    }


    // 定义一个密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // 重写该方法之后必须指定认证方式，否则请求的时候 -u user:pwd 提交了数据也无法知道用户是谁
        http.httpBasic();
    }
}
