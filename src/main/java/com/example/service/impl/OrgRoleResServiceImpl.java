package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgRoleAuthPO;
import com.example.bean.OrgRoleResPO;
import com.example.mapper.OrgRoleResDao;
import com.example.service.OrgRoleAuthService;
import com.example.service.OrgRoleResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pxh
 * @since 2023-04-23 10:10:24
 */
@Service
public class OrgRoleResServiceImpl extends ServiceImpl<OrgRoleResDao, OrgRoleResPO> implements OrgRoleResService {
    private OrgRoleAuthService orgRoleAuthService;
    @Override
    public List<OrgRoleResPO> getResByRoles() {
        List<OrgRoleAuthPO> userRoles = orgRoleAuthService.getUserRoles();
        List<Long> roleIds = userRoles.stream().map(OrgRoleAuthPO::getRoleId).collect(Collectors.toList());
        return getResByRoles(roleIds);
    }

    @Override
    public List<OrgRoleResPO> getResByRoles(Long roleId) {
        List<Long> roleIds=new ArrayList<>();
        roleIds.add(roleId);
        return getResByRoles(roleIds);
    }

    @Override
    public List<OrgRoleResPO> getResByRoles(List<Long> roleIds) {
        if(roleIds==null||roleIds.size()==0){
            return new ArrayList<>();
        }
        LambdaQueryWrapper lq = Wrappers.<OrgRoleResPO>lambdaQuery()
                .in(OrgRoleResPO::getRoleId,roleIds);
        return this.list(lq);
    }

    @Autowired
    public void setOrgRoleAuthService(OrgRoleAuthService orgRoleAuthService) {
        this.orgRoleAuthService = orgRoleAuthService;
    }
}
