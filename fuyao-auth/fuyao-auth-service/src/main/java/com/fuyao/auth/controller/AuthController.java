package com.fuyao.auth.controller;

import com.fuyao.auth.config.JwtProperties;
import com.fuyao.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
}
