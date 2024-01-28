package com.example.event.events;


import com.example.bean.OrgUserPO;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.event.BaseEvent;
import org.springframework.stereotype.Service;

@Service("UserRemoveEvent")
public class UserRemoveEvent implements BaseEvent {

    private AddOrEditUserResponse UserResponse;

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

    public AddOrEditUserResponse getUserResponse() {
        return UserResponse;
    }

    public void setUserResponse(AddOrEditUserResponse UserResponse) {
        this.UserResponse = UserResponse;
    }

    @Override
    public String getEventName() {
        return "UserRemoveEvent";
    }
}
