package com.fuyao.user.service;

import com.fuyao.common.model.RedisConstants;
import com.fuyao.common.utils.CodecUtils;
import com.fuyao.user.mapper.UserMapper;
import com.fuyao.user.pojo.User;
import org.omg.IOP.Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class UserService {
    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUserData(String data, Integer type) {
        User user = new User();
        if(type == 1){
            //校验用户名
            user.setUsername(data);
        }else if(type == 2){
            //校验手机号
            user.setPhone(data);
        }else{
            return null;
        }
        int i = userMapper.selectCount(user);
        return i == 0;
    }


    /**
     * 根据用户名和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    public User queryUserByNameAndPwd(String username, String password) {
        // 校验用户名
        if(username == null){
            return null;
        }

        //获取盐salt
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if(user == null){
            return null;
        }
        String salt = user.getSalt();
        //校验密码
        String s = CodecUtils.md5Hex(password, salt);
        if(!user.getPassword().equals(s)){
            return null;
        }
        //用户名和密码正确
        return user;
    }

    /**
     * 注册用户
     * @param user
     * @param code
     * @return
     */
    @Transactional
    public Boolean registerUser(User user, String code) {
        //校验短信验证码,从redis中读取code
        String redisSmsCode = redisTemplate.opsForValue().get(RedisConstants.REDIS_SMS_CODE + user.getPhone());
        if(redisSmsCode == null){
            return false;
        }
        if(!redisSmsCode.equals(code)){
            logger.error("验证码错误！");
            return false;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //对password进行md5加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());
        //添加到数据库
        Boolean boo = userMapper.insertSelective(user) == 1;
        if(boo){
            //注册成功，删除redis中的记录
            redisTemplate.delete(RedisConstants.REDIS_SMS_CODE + user.getPhone());
        }
        return boo;
    }
}
