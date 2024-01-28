package com.example.controller;


import com.example.service.OrgRoleAuthService;
import com.example.service.OrgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssj
 * @since 2023-04-23 10:10:24
 */
@RestController
@RequestMapping("/roleAuth")
public class OrgRoleAuthController {

    @Autowired
    private OrgRoleAuthService orgRoleAuthService;

    @Autowired
    private OrgRoleService orgRoleService;

}
