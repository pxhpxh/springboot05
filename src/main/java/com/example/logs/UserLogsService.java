package com.example.logs;


import com.example.annotation.ListenEvent;
import com.example.bean.LogPO;
import com.example.bean.OrgUserPO;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.event.events.UserAddEvent;
import com.example.event.events.UserEditEvent;
import com.example.event.events.UserLoginEvent;
import com.example.event.events.UserRemoveEvent;
import com.example.service.LogService;
import com.example.service.OrgUserService;
import com.example.util.POComparator;
import com.example.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserLogsService {

    @Autowired
    private LogService logService;

    @Autowired
    private OrgUserService userService;

    @ListenEvent(event = UserLoginEvent.class)
    public void OnUserLoginEvent(UserLoginEvent event){
        //String json= JSONUtil.toJSONString(event.getLoginRequest());
        //System.out.println("异步调用CreateUserEvent:"+json);
    }

    @ListenEvent(event = UserAddEvent.class)
    public void userAddEvent(UserAddEvent event) {
        OrgUserPO userPO = event.getUserPO();
        OrgUserPO loginUser = event.getLoginUser();
        Long corpId = event.getCorpId();
        Long groupId = event.getGroupId();

        LogPO logPO = new LogPO();
        logPO.setId(UUIDUtils.longUUID());
        logPO.setGroupId(groupId);
        logPO.setCorpId(corpId);
        LocalDateTime now = LocalDateTime.now();
        logPO.setCreateTime(now);
        logPO.setType(1);
        logPO.setTypeSub(3);
        logPO.setDeleted(false);
        logPO.setState(true);
        logPO.setDataStr(loginUser.getName() + "新增用户：【" + userPO.getName() + "】，成功！");
        logService.saveLog(logPO);
    }

    @ListenEvent(event = UserEditEvent.class)
    public void userEditEvent(UserEditEvent event) {
        OrgUserPO userPO = event.getUserPO();
        OrgUserPO loginUser = event.getLoginUser();
        Long corpId = event.getCorpId();
        Long groupId = event.getGroupId();

        OrgUserPO oldUserPO = userService.getById(userPO.getId());

        LogPO logPO = new LogPO();
        logPO.setId(UUIDUtils.longUUID());
        logPO.setGroupId(groupId);
        logPO.setCorpId(corpId);
        LocalDateTime now = LocalDateTime.now();
        logPO.setCreateTime(now);
        logPO.setType(1);
        logPO.setTypeSub(3);
        logPO.setDeleted(false);
        logPO.setState(true);
        String changes = POComparator.getChanges(oldUserPO, userPO);
        logPO.setDataStr(loginUser.getName() + "编辑用户：【" + changes + "】，成功！");
        logService.saveLog(logPO);
    }


    @ListenEvent(event = UserRemoveEvent.class)
    public void userRemoveEvent(UserRemoveEvent event) {
        AddOrEditUserResponse userResponse = event.getUserResponse();
        OrgUserPO loginUser = event.getLoginUser();
        Long corpId = event.getCorpId();
        Long groupId = event.getGroupId();

        LogPO logPO = new LogPO();
        logPO.setId(UUIDUtils.longUUID());
        logPO.setGroupId(groupId);
        logPO.setCorpId(corpId);
        LocalDateTime now = LocalDateTime.now();
        logPO.setCreateTime(now);
        logPO.setType(1);
        logPO.setTypeSub(3);
        logPO.setDeleted(false);
        logPO.setState(true);
        logPO.setDataStr(loginUser.getName() + "删除用户：【" + userResponse.getName() + "】，成功！");
        logService.saveLog(logPO);
    }
}
