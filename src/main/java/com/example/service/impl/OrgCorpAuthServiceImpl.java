package com.example.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgCorpAuthPO;
import com.example.bean.OrgCorpPO;
import com.example.bean.OrgUserPO;
import com.example.mapper.OrgCorpAuthDao;
import com.example.service.LoginService;
import com.example.service.OrgCorpAuthService;
import com.example.service.OrgCorpService;
import com.example.service.OrgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgCorpAuthServiceImpl extends ServiceImpl<OrgCorpAuthDao, OrgCorpAuthPO> implements OrgCorpAuthService {
    private LoginService loginService;
    private OrgCorpService orgCorpService;
    private OrgUserService userService;

    @Override
    public List<OrgCorpPO> getCurUserCorps() {
        OrgUserPO user=loginService.getLoginUser();
        if(user!=null){
            return getUserCorps(user.getId());
        }
        return null;
    }

    @Override
    public List<OrgCorpPO> getUserCorps(Long userId) {
        List<OrgCorpPO> list=new ArrayList<>();
        OrgUserPO user = userService.getById(userId);
        if(user!=null){
            OrgCorpPO userCorp = orgCorpService.getCorpById(user.getCorpId());
            list.add(userCorp);

            LambdaQueryWrapper<OrgCorpAuthPO> lqw=new LambdaQueryWrapper<>();
            lqw.eq(OrgCorpAuthPO::getUserId,user.getId());
            List<OrgCorpAuthPO> corpAuthList = this.list(lqw);
            corpAuthList.forEach(orgCorpAuthPO -> {
                OrgCorpPO temCorp = orgCorpService.getCorpById(orgCorpAuthPO.getCorpId());
                list.add(temCorp);
            });
        }
        return list;
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
    @Autowired
    public void setOrgCorpService(OrgCorpService orgCorpService) {
        this.orgCorpService = orgCorpService;
    }

    @Autowired
    public void setUserService(OrgUserService userService) {
        this.userService = userService;
    }
}
