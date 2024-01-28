package com.example.controller;


import com.example.common.ResponseData;
import com.example.config.RedisConfig;
import com.example.dto.authentication.LoginUser;
import com.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class KeepaLiveController {
    private LoginService loginService;
    private RedisTemplate redisTemplate;
    private static final String REDIS_PRE = "token:";

    @RequestMapping("/keepalive")
    public ResponseData keep(){
        LoginUser loginUser=loginService.getSecurityLoginUser();
        if(loginUser!=null) {
            redisTemplate.expire(REDIS_PRE + loginUser.getUid(), RedisConfig.REDIS_TIMEOUT, TimeUnit.MINUTES);
            return ResponseData.success();
        }
        return ResponseData.error("用户已下线");
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
