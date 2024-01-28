package com.example.service;


import com.example.bean.OrgUserPO;
import com.example.common.ResponseData;
import com.example.dto.authentication.*;
import com.example.exception.BaseBusinessException;

public interface LoginService {
    ResponseData<LoginResponse> login(LoginRequest login);
    ResponseData<UserResponse> getUserByToken();
    ResponseData<String> logout();
    OrgUserPO getLoginUser();
    LoginUser getSecurityLoginUser();
    Long getLoginUserId();
    void changeCurrentCorp(Long corpId) throws BaseBusinessException;
    Long getCurrentGroupId();
    Long getCurrentCorpId();
    LoginUserRedis getCurrentUserRedis();
}
