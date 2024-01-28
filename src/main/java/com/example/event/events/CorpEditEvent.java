package com.example.event.events;


import com.example.bean.OrgCorpPO;
import com.example.bean.OrgUserPO;
import com.example.event.BaseEvent;
import org.springframework.stereotype.Service;

@Service("CorpEditEvent")
public class CorpEditEvent implements BaseEvent {

    private OrgCorpPO corpPO;

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

    public OrgCorpPO getCorpPO() {
        return corpPO;
    }

    public void setCorpPO(OrgCorpPO corpPO) {
        this.corpPO = corpPO;
    }

    @Override
    public String getEventName() {
        return "CorpEditEvent";
    }
}
