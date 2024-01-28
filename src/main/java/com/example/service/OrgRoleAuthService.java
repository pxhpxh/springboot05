package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.OrgRoleAuthPO;
import com.example.dto.org.AddOrEditUserResponse;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pxh
 * @since 2023-04-23 10:10:24
 */
public interface OrgRoleAuthService extends IService<OrgRoleAuthPO> {
    List<OrgRoleAuthPO> getUserRoles();
    List<OrgRoleAuthPO> getUserRoles(Long userId);
    void saveOrEdit(AddOrEditUserResponse userResponse);
    void removeByUserResponse(AddOrEditUserResponse userResponse);
    List<OrgRoleAuthPO> getUserRoles(Long userId, Long groupId, Long corpId);
}
