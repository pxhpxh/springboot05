package com.example.controller;


import com.example.bean.OrgUserPO;
import com.example.common.ResponseData;
import com.example.dto.authentication.LoginRequest;
import com.example.dto.authentication.LoginResponse;
import com.example.dto.grid.Pager;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.dto.org.EditPasswordRequest;
import com.example.exception.BaseBusinessException;
import com.example.service.LoginService;
import com.example.service.OrgRoleAuthService;
import com.example.service.OrgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 * 用户认证及管理
 */
@RestController
@RequestMapping("/user")
public class OrgUserController {
    @Autowired
    private OrgUserService userService;

    @Autowired
    private OrgRoleAuthService roleAuthService;

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录接口
     * @param login 请求对象
     * @return 登录成功返回token
     */
    @PostMapping("/login")
    public ResponseData<LoginResponse> login(@RequestBody LoginRequest login){
        //登录
        return loginService.login(login);
    }

    @RequestMapping("/info")
    public ResponseData info(){
        return loginService.getUserByToken();
    }

    @RequestMapping("/changeCurrentCorp")
    public ResponseData changeCurrentCorp(@RequestParam Long corpId){
        try {
            loginService.changeCurrentCorp(corpId);
            return ResponseData.success();
        } catch (BaseBusinessException e){
            return ResponseData.error(e.getMessage());
        }
    }
    /**
     * 退出登录接口
     * @return 返回退出状态
     */
    @RequestMapping("/logout")
    public ResponseData logout(){
        return loginService.logout();
    }

    @RequestMapping("/remove")
    public ResponseData remove(@RequestBody AddOrEditUserResponse userResponse){
        OrgUserPO del_user=userService.getById(userResponse.getId());
        boolean ret=userService.removeById(userResponse.getId());
        if(ret){
            userService.removeTokenByLoginName(del_user.getLoginName());
            roleAuthService.removeByUserResponse(userResponse);
            return ResponseData.success();
        } else {
            return ResponseData.error(-1,"用户删除失败");
        }
    }

    @RequestMapping("/saveOrEdit")
    public ResponseData saveOrEdit(@RequestBody AddOrEditUserResponse addUser){
        try {
            userService.saveOrEdit(addUser);
            return ResponseData.success(addUser);
        } catch (BaseBusinessException e){
            return ResponseData.error(-1,e.getMessage());
        }
    }

    @RequestMapping("/editPassword")
    public ResponseData editPassword(@RequestBody EditPasswordRequest editPassword){
        try {
            editPassword.setId(loginService.getLoginUserId());//只允许修改当前用户密码
            userService.editPassword(editPassword);
            return ResponseData.success();
        } catch (BaseBusinessException e){
            return ResponseData.error(-1,e.getMessage());
        }
    }

    /**
     * 获取人员列表
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody Pager<OrgUserPO> pager){
        try {
            Pager orgUserPOPager = userService.getPage(pager);
            return ResponseData.success(orgUserPOPager);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }


}
