package com.fuyao.auth.test;

import com.fuyao.auth.entity.UserInfo;
import com.fuyao.auth.utils.JwtUtils;
import com.fuyao.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    private static final String pubKeyPath = "F:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "F:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    /**
     * 生成公钥和密钥
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "@#Pass567");
    }

    /**
     * 创建目录
     */
    //@Before
    public void testCreatePath(){
        File file = new File("F:\\tmp\\rsa");
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }
    }

    @Before //获取公钥和私钥
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 生成token,并设置存活时间
     * @throws Exception
     */
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token,过期时间5分钟
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 1);
        System.out.println("token = " + token);
    }

    /**
     * 解析token
     * @throws Exception
     */
    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYwMjY2Njk0NX0.p4fQ88x8enW_D1_-YHjxP-VvOfCG9aq-5OouO57VitWBWrklb9KkQjI7xZWm1D8zzOvbwJMrxuldryIDXsFfXhexUzvzC6PR8nERm97-zOaJx2y-hCejwcfgoULHnCv2GsRJ1rPX3g5VCmHPCXRFKgs4jZbQFQ6aOg3wkfKeobM";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
