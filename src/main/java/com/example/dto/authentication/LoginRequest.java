package com.example.dto.authentication;

import com.example.common.AbstractRequest;
import lombok.Data;


@Data
public class LoginRequest extends AbstractRequest {
    //登录名
    private String username;
    //登录密码
    private String password;
    //验证码序号
    private String cId;
    //验证码
    private String verificationCode;
}
