package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgRoleAuthPO;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.mapper.OrgRoleAuthDao;
import com.example.service.LoginService;
import com.example.service.OrgRoleAuthService;
import com.example.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pxh
 * @since 2023-04-23 10:10:24
 */
@Service
public class OrgRoleAuthServiceImpl extends ServiceImpl<OrgRoleAuthDao, OrgRoleAuthPO> implements OrgRoleAuthService {
    private LoginService loginService;

    @Override
    public List<OrgRoleAuthPO> getUserRoles() {
        return getUserRoles(loginService.getLoginUserId());
    }

    @Override
    public List<OrgRoleAuthPO> getUserRoles(Long userId) {
        LambdaQueryWrapper lq = Wrappers.<OrgRoleAuthPO>lambdaQuery()
                .eq(OrgRoleAuthPO::getUserId, userId);
        return this.list(lq);
    }

    @Override
    public void saveOrEdit(AddOrEditUserResponse userResponse) {
        userResponse.setGroupId(loginService.getCurrentGroupId());
        userResponse.setCorpId(loginService.getCurrentCorpId());
        if (userResponse.getId() != null) {
            LambdaQueryWrapper<OrgRoleAuthPO> lqwRemove = Wrappers.<OrgRoleAuthPO>lambdaQuery()
                    .eq(OrgRoleAuthPO::getUserId, userResponse.getId())
                    .eq(OrgRoleAuthPO::getCorpId, userResponse.getCorpId())
                    .eq(OrgRoleAuthPO::getGroupId, userResponse.getGroupId());
            this.remove(lqwRemove);
        }
        this.saveAuthByUserResponse(userResponse);
    }

    @Override
    public void removeByUserResponse(AddOrEditUserResponse userResponse) {
        if (userResponse.getId() != null) {
            LambdaQueryWrapper<OrgRoleAuthPO> lqwRemove = Wrappers.<OrgRoleAuthPO>lambdaQuery()
                    .eq(OrgRoleAuthPO::getUserId, userResponse.getId())
                    .eq(OrgRoleAuthPO::getCorpId, userResponse.getCorpId())
                    .eq(OrgRoleAuthPO::getGroupId, userResponse.getGroupId());
            this.remove(lqwRemove);
        }
    }

    @Override
    public List<OrgRoleAuthPO> getUserRoles(Long userId, Long groupId, Long corpId) {
        LambdaQueryWrapper lq = Wrappers.<OrgRoleAuthPO>lambdaQuery()
                .eq(OrgRoleAuthPO::getUserId, userId)
                .eq(OrgRoleAuthPO::getCorpId, groupId)
                .eq(OrgRoleAuthPO::getGroupId, corpId);
        return this.list(lq);
    }

    public void saveAuthByUserResponse(AddOrEditUserResponse userResponse) {
        String[] roleIDs = userResponse.getRole();
        for (String roleId : roleIDs) {
            OrgRoleAuthPO savePO = new OrgRoleAuthPO();
            savePO.setId(UUIDUtils.longUUID());
            savePO.setRoleId(Long.valueOf(roleId));
            savePO.setUserId(userResponse.getId());
            savePO.setCorpId(userResponse.getCorpId());
            savePO.setGroupId(userResponse.getGroupId());
            savePO.setCreateTime(LocalDateTime.now());
            savePO.setDeleted(false);
            this.save(savePO);
        }
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}
