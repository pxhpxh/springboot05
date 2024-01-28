package com.example.service;


import com.example.bean.OrgCorpPO;

import java.util.List;

public interface OrgCorpAuthService {
    List<OrgCorpPO> getCurUserCorps();
    List<OrgCorpPO> getUserCorps(Long userId);
}
