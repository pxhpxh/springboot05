package com.example.event.events;

import com.example.bean.OrgUserPO;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.event.BaseEvent;
import org.springframework.stereotype.Service;

@Service("UserAddEvent")
public class UserAddEvent implements BaseEvent {

    private OrgUserPO UserPO;
    private AddOrEditUserResponse userResponse;

    private OrgUserPO loginUser;

    private Long groupId;

    private Long corpId;

    public OrgUserPO getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(OrgUserPO loginUser) {
        this.loginUser = loginUser;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getCorpId() {
        return corpId;
    }

    public void setCorpId(Long corpId) {
        this.corpId = corpId;
    }

    public OrgUserPO getUserPO() {
        return UserPO;
    }

    public void setUserPO(OrgUserPO UserPO) {
        this.UserPO = UserPO;
    }

    public AddOrEditUserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(AddOrEditUserResponse userResponse) {
        this.userResponse = userResponse;
    }

    @Override
    public String getEventName() {
        return "UserAddEvent";
    }
}
