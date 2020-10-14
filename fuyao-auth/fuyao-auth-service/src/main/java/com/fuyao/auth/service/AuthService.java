package com.fuyao.auth.service;

import com.fuyao.auth.client.UserClient;
import com.fuyao.auth.config.JwtProperties;
import com.fuyao.auth.entity.UserInfo;
import com.fuyao.auth.utils.JwtUtils;
import com.fuyao.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录校验
     * @param username
     * @param password
     * @return
     */
    public String authentication(String username, String password){
        try {
            //调用user微服务校验用户名和密码是否正确
            User user = userClient.queryUserByNameAndPwd(username, password);
            if(user == null){
                return null;
            }
            //jwtUtils生成jwt类型的token
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), 30);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
