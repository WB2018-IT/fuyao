package com.fuyao.config;

import com.fuyao.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "fuyao.jwt")
public class JwtProperties {
    private String cookieName;
    private String pubKeyPath;
    private PublicKey publicKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);


    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥失败!",e);
            throw new RuntimeException();
        }
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
