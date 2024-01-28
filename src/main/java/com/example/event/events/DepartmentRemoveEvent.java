package com.example.event.events;

import com.example.bean.OrgUserPO;
import com.example.event.BaseEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DepartmentRemoveEvent")
public class DepartmentRemoveEvent implements BaseEvent {

    private List<Long> idList;

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

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    @Override
    public String getEventName() {
        return "DepartmentRemoveEvent";
    }
}
