package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgRolePO;
import com.example.exception.BaseBusinessException;
import com.example.mapper.OrgRoleDao;
import com.example.service.LoginService;
import com.example.service.OrgRoleService;
import com.example.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pxh
 * @since 2023-04-23 10:02:44
 */
@Service
public class OrgRoleServiceImpl extends ServiceImpl<OrgRoleDao, OrgRolePO> implements OrgRoleService {

    @Autowired
    private LoginService loginService;

    /**
     * 初始化单位或集团默认角色
     *
     * @param groupId 集团id
     * @param corpId  单位id(单位id为0时为集团初始化)
     * @throws BaseBusinessException 返回报错原因
     */
    @Transactional
    @Override
    public void corpRolesInit(long groupId, long corpId) throws BaseBusinessException {
        LambdaQueryWrapper<OrgRolePO> roleQW = new LambdaQueryWrapper<>();
        roleQW.eq(OrgRolePO::getDeleted, false)
                .eq(OrgRolePO::getGroupId, (corpId == 0) ? 0L : groupId)
                .eq(OrgRolePO::getCorpId, 0L);

        List<OrgRolePO> roles = this.list(roleQW);
        List<OrgRolePO> newRoles = new ArrayList<>();
        for (OrgRolePO role : roles) {
            OrgRolePO newRole = new OrgRolePO();
            newRole.setId(UUIDUtils.longUUID());
            newRole.setName(role.getName());
            newRole.setCode(role.getCode());
            newRole.setState(1);
            newRole.setGroupId(groupId);
            newRole.setCorpId(corpId);
            newRole.setCreateTime(LocalDateTime.now());
            newRole.setDeleted(false);
            newRoles.add(newRole);
        }
        if (newRoles.size() > 0) {
            boolean isSuccess = this.saveBatch(newRoles);
            if (!isSuccess) {
                throw new BaseBusinessException("初始化单位角色失败");
            }
        } else {
            throw new BaseBusinessException("初始化单位角色失败");
        }
    }

    /**
     * 删除单位所有角色（删除单位时使用）
     *
     * @param groupId 集团id
     * @param corpId  单位id
     * @throws BaseBusinessException
     */
    @Transactional
    @Override
    public void delCropAllRoles(Long groupId, Long corpId) throws BaseBusinessException {
        LambdaUpdateWrapper<OrgRolePO> roleUQ = new LambdaUpdateWrapper<>();
        roleUQ.eq(OrgRolePO::getDeleted, false)
                .eq(OrgRolePO::getGroupId, groupId)
                .eq(OrgRolePO::getCorpId, corpId)
                .set(OrgRolePO::getDeleted, true);
        boolean isSuccess = this.update(roleUQ);
        if (!isSuccess) {
            throw new BaseBusinessException("删除单位角色失败");
        }
    }

    /**
     * 获取当前单位或集团所有的角色
     *
     * @return 返回当前单位或集团所有的角色
     * @throws BaseBusinessException
     */
    public List<OrgRolePO> getRoles() throws BaseBusinessException {
        LambdaQueryWrapper<OrgRolePO> roleQW = new LambdaQueryWrapper<>();
        Long groupId = loginService.getCurrentGroupId();
        Long corpId = loginService.getCurrentCorpId();
        roleQW.eq(OrgRolePO::getGroupId, groupId)
                .eq(OrgRolePO::getCorpId, corpId)
                .or()
                .eq(OrgRolePO::getGroupId, 0)
                .eq(OrgRolePO::getCorpId, 0);
        List<OrgRolePO> roles = null;
        try {
            roles = this.list(roleQW);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseBusinessException("获取单位角色出错");
        }
        return roles;
    }

}
