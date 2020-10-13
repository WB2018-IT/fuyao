package com.fuyao.user.controller;

import com.fuyao.user.pojo.User;
import com.fuyao.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean>checkUserDate(@PathVariable("data") String data,@PathVariable("type") Integer type){
        Boolean boo = userService.checkUserData(data,type);
        if(boo == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boo);
    }

    /**
     * 根据用户名和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUserByNameAndPwd(@RequestParam("username") String username,@RequestParam("password") String password){
        User user = userService.queryUserByNameAndPwd(username,password);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


    /**
     * 注册用户
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> registerUser(@Valid User user,@RequestParam("code") String code){
        Boolean boo = userService.registerUser(user,code);
        if(boo == null || !boo){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据手机验证码修改密码
     * @param username
     * @param password
     * @param code
     * @return
     */
    @PutMapping("edit")
    public ResponseEntity<Void> editPasswdByCode(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("phone") String phone,@RequestParam("code") String code){
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)){
            return ResponseEntity.badRequest().build();
        }
        Boolean boo = userService.editPasswdByCode(username,password,phone,code);
        if(boo == null || !boo){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
