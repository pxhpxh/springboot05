package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.OrgRoleResPO;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pxh
 * @since 2023-04-23 10:10:24
 */
public interface OrgRoleResService extends IService<OrgRoleResPO> {
    List<OrgRoleResPO> getResByRoles();
    List<OrgRoleResPO> getResByRoles(Long roleId);
    List<OrgRoleResPO> getResByRoles(List<Long> roleIds);
}
