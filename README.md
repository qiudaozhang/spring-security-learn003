# 指导文档



## 安全配置



```java
package com.qiudaozhang.springsecuritylearn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


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
}
```



## 测试控制器

```java
package com.qiudaozhang.springsecuritylearn.controller;

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

}

```





## 测试

```bash
curl   -u admin:123 "http://localhost:8080/auth/test"
```



```bash
curl -u admin:123 -XPOST "http://localhost:8080/auth/post"
```



## post请求无法通过

spring security提供了csrf保护。



临时禁用

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
}
```





## 访问Authentication



```java
@PostMapping("auth/authentication")
public String authentication() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    System.out.println(authentication);
    return "authentication";
}
```



```bash
curl -u admin:123   "http://localhost:8080/auth/authentication"
```



得到是

```
AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_ANONYMOUS]]
```

说明当前模式下上下文无法得到它。



### 在默认随机密码机制下

```bash
curl -u user:51dda494-6320-4999-ab55-0ed7688131d6 "http://localhost:8080/auth/authentication"
```



```
UsernamePasswordAuthenticationToken [Principal=org.springframework.security.core.userdetails.User [Username=user, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[]], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[]]
```

username可以获取为user

它是一个UsernamePasswordAuthenticationToken



### 自定义用户下

```
curl -u admin:123 "http://localhost:8080/auth/authentication"
```



```
UsernamePasswordAuthenticationToken [Principal=org.springframework.security.core.userdetails.User [Username=admin, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[read]], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[read]]
```

可以获取





## 管理用户



### 创建用户

#### 控制器

```java
package com.qiudaozhang.springsecuritylearn.controller;

import com.qiudaozhang.springsecuritylearn.req.UserReq;
import com.qiudaozhang.springsecuritylearn.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 }
```





#### 请求实体

```java
package com.qiudaozhang.springsecuritylearn.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 邱道长
 * 2020/12/24
 */

@Setter
@Getter
public class UserReq {

    private String username;

    private String password;


}
```





#### 业务接口

```java
package com.qiudaozhang.springsecuritylearn.service;

import com.qiudaozhang.springsecuritylearn.req.UserReq;

/**
 * @author 邱道长
 * 2020/12/24
 */
public interface UserService {
    void createUser(UserReq req);
}

```



#### 实现

```java
package com.qiudaozhang.springsecuritylearn.service.impl;

import com.qiudaozhang.springsecuritylearn.req.UserReq;
import com.qiudaozhang.springsecuritylearn.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
```





#### 测试

```bash
curl -v -XPOST -u admin:123 -d "{\"username\":\"dao\",\"password\":\"123\"}" "http://localhost:8080/manage/user" -H "Content-Type:application/json"
```



### 查询用户

#### 控制器

```java
@GetMapping("user/{username}")
public ServerResponse<UserDetails> findUser(@PathVariable("username") String username) {
    return userService.find(username);
}
```





#### 通用返回对象



```java
package com.qiudaozhang.springsecuritylearn.commom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author 邱道长
 * 2020/12/24
 */
@Getter
@Setter
public class ServerResponse <T>{


    private T data;

    private int code;

    private String msg;

    final static int CODE_SUCCESS = 0;
    final static int CODE_FAIL = 1;


    final static String MESSAGE_SUCCESS = "success";


    private ServerResponse(int code , String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> ServerResponse<T> success(){
        return new ServerResponse<>(CODE_SUCCESS,MESSAGE_SUCCESS);
    }

    public static <T> ServerResponse<T> success(T data){
        return new ServerResponse<T>(data,CODE_SUCCESS,MESSAGE_SUCCESS);
    }
    public static <T> ServerResponse<T> error(String msg){
        return new ServerResponse<T>(CODE_FAIL,msg);
    }
}

```



#### 业务实现

```java
@Override
public ServerResponse<UserDetails> find(String username) {

    boolean b = udm.userExists(username);
    if(b) {
        UserDetails userDetails = udm.loadUserByUsername(username);

        return ServerResponse.success(userDetails);
    } else {
        return ServerResponse.error("用户不存在！");
    }
}
```



#### 测试

先增加一个用户

```bash
curl -v -XPOST -u admin:123 -d "{\"username\":\"dao\",\"password\":\"123\"}" "http://localhost:8080/manage/user" -H "Content-Type:application/json"
```



```bash
curl  -u admin:123  "http://localhost:8080/manage/user/dao"  
```





### 修改密码等等

代码差不多，具体参考源码即可





````bash
curl -v -XPOST -u admin:123 -d "{\"username\":\"dao\",\"password\":\"123\"}" "http://localhost:8080/manage/user" -H "Content-Type:application/json"
````





```bash
curl -v -XPUT -u dao:123 -d "{\"oldPassword\":\"123\",\"newPassword\":\"abc123\"}" "http://localhost:8080/manage/user/changepassword" -H "Content-Type:application/json"
```



```bash
curl -u dao:123 -XGET "http://localhost:8080/auth/authentication"
```



### 删除用户

```bash
curl   -XPOST -u admin:123 -d "{\"username\":\"dao\",\"password\":\"123\"}" "http://localhost:8080/manage/user" -H "Content-Type:application/json"
```



```bash
curl     -u admin:123   "http://localhost:8080/manage/user/dao"  
```



```bash
curl  -XDELETE -u admin:123   "http://localhost:8080/manage/user/dao"  
```

