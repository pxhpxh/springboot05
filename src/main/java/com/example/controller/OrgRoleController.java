package com.example.controller;


import com.example.bean.OrgRolePO;
import com.example.common.ResponseData;
import com.example.exception.BaseBusinessException;
import com.example.service.OrgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ssj
 * @since 2023-04-23 10:02:44
 */
@RestController
@RequestMapping("/role")
public class OrgRoleController {

    @Autowired
    private OrgRoleService orgRoleService;

    /**
     * @return 前端的下拉列表获取选项
     */
    @RequestMapping("/selectOptions")
    public ResponseData list() {
        try {
            List<OrgRolePO> roles = orgRoleService.getRoles();
            return ResponseData.success(roles);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }



}
