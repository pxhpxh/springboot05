package com.example.event.events;


import com.example.bean.OrgDepartmentPO;
import com.example.bean.OrgUserPO;
import com.example.event.BaseEvent;
import org.springframework.stereotype.Service;

@Service("DepartmentAddEvent")
public class DepartmentAddEvent implements BaseEvent {

    private OrgDepartmentPO DepartmentPO;

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

    public OrgDepartmentPO getDepartmentPO() {
        return DepartmentPO;
    }

    public void setDepartmentPO(OrgDepartmentPO DepartmentPO) {
        this.DepartmentPO = DepartmentPO;
    }

    @Override
    public String getEventName() {
        return "DepartmentAddEvent";
    }
}
