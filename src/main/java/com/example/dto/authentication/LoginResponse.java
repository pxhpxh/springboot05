package com.example.dto.authentication;

import lombok.Data;


@Data
public class LoginResponse {
    private static final long serialVersionUID = 7200739375968987803L;
    //令牌
    private String token;
}
