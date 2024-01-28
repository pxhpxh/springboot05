package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.OrgRolePO;
import com.example.exception.BaseBusinessException;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pxh
 * @since 2023-04-23 10:02:44
 */
public interface OrgRoleService extends IService<OrgRolePO> {
    /**
     * 初始化单位或集团默认角色
     * @param groupId 集团id
     * @param corpId 单位id(单位id为0时为集团初始化)
     * @throws BaseBusinessException 返回报错原因
     */
    void corpRolesInit(long groupId,long corpId) throws BaseBusinessException;

    /**
     * 删除单位所有角色（删除单位时使用）
     * @param groupId 集团id
     * @param corpId 单位id
     * @throws BaseBusinessException
     */
    void delCropAllRoles(Long groupId, Long corpId) throws BaseBusinessException;

    /**
     * 获取当前单位或集团所有的角色
     * @return 返回单位下所有角色
     * @throws BaseBusinessException
     */
    List<OrgRolePO> getRoles() throws BaseBusinessException;
}
