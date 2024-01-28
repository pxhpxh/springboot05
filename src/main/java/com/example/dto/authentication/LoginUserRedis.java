package com.example.dto.authentication;

import com.example.bean.OrgCorpPO;
import lombok.Data;

import java.util.List;

@Data
public class LoginUserRedis {
    private LoginUser loginUser;
    private List<OrgCorpPO> corps;
    private Long currentCorpId;
}
