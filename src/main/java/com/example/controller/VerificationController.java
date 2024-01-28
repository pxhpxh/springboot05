package com.example.controller;


import com.example.common.ResponseData;
import com.example.util.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class VerificationController {
    private RedisTemplate redisTemplate;
    private static final String REDIS_PRE = "verify_code:";
    private static final int VERIFY_CODE_TIME_OUT=3;//单位分钟

    @GetMapping("/verifyCode")
    public ResponseData VerifyCode(){
        Map<String, String> image = VerificationCode.getImage();
        String code = image.remove("code");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(REDIS_PRE + image.get("id"), code, VERIFY_CODE_TIME_OUT, TimeUnit.MINUTES);
        return ResponseData.success(image);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
