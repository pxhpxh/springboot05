package com.example.controller;

import com.example.common.ResponseData;
import com.example.dto.RouterItem;
import com.example.dto.grid.Pager;
import com.example.service.LoginService;
import com.example.service.OrgUserService;
import com.example.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssj
 * @since 2023-04-14 15:58:36
 */
@RestController
@RequestMapping("/router")
public class RouterController {
    private OrgUserService userService;
    private LoginService loginService;
    private RouterService routerService;

    /**
     * 获取当前人员路由列表
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(){
        List<RouterItem> routers = routerService.getRoutersVO();
        return ResponseData.success(routers);
    }

    @PostMapping("/AllList")
    public ResponseData getAllList(@RequestBody Pager pager){
        List<RouterItem> routers =routerService.getAllRoutersVO();
        pager.setPageData(routers);
        pager.getFlipInfo().setTotal(routers.size());
        return ResponseData.success(pager);
    }

    @Autowired
    public void setUserService(OrgUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setRouterService(RouterService routerService) {
        this.routerService = routerService;
    }
}
