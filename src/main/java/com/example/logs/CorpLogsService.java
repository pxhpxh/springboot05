package com.example.logs;


import com.example.annotation.ListenEvent;
import com.example.bean.LogPO;
import com.example.bean.OrgCorpPO;
import com.example.bean.OrgUserPO;
import com.example.event.events.CorpAddEvent;
import com.example.event.events.CorpEditEvent;
import com.example.event.events.CorpRemoveEvent;
import com.example.service.LogService;
import com.example.service.OrgCorpService;
import com.example.util.POComparator;
import com.example.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorpLogsService {

    @Autowired
    private OrgCorpService corpService;

    @Autowired
    private LogService logService;

    @ListenEvent(event = CorpAddEvent.class)
    public void corpAddEvent(CorpAddEvent event) {
        OrgCorpPO corpPO = event.getCorpPO();
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
        logPO.setTypeSub(1);
        logPO.setDeleted(false);
        logPO.setState(true);
        logPO.setDataStr(loginUser.getName() + "新增单位：【" + corpPO.getName() + "】，成功！");
        logService.saveLog(logPO);
    }

    @ListenEvent(event = CorpEditEvent.class)
    public void corpEditEvent(CorpEditEvent event) {
        OrgCorpPO corpPO = event.getCorpPO();
        OrgUserPO loginUser = event.getLoginUser();
        Long corpId = event.getCorpId();
        Long groupId = event.getGroupId();

        OrgCorpPO oldCorpPO = corpService.getCorpById(corpPO.getId());

        LogPO logPO = new LogPO();
        logPO.setId(UUIDUtils.longUUID());
        logPO.setGroupId(groupId);
        logPO.setCorpId(corpId);
        LocalDateTime now = LocalDateTime.now();
        logPO.setCreateTime(now);
        logPO.setType(1);
        logPO.setTypeSub(1);
        logPO.setDeleted(false);
        logPO.setState(true);
        String changes = POComparator.getChanges(oldCorpPO, corpPO);
        logPO.setDataStr(loginUser.getName() + "编辑单位：【" + changes + "】，成功！");
        logService.saveLog(logPO);
    }


    @ListenEvent(event = CorpRemoveEvent.class)
    public void corpRemoveEvent(CorpRemoveEvent event) {
        List<Long> idList = event.getIdList();
        OrgUserPO loginUser = event.getLoginUser();
        Long corpId = event.getCorpId();
        Long groupId = event.getGroupId();

        List<LogPO> logPOList = new ArrayList<>();
        for (Long id : idList) {
            LogPO logPO = new LogPO();
            logPO.setId(UUIDUtils.longUUID());
            logPO.setGroupId(groupId);
            logPO.setCorpId(corpId);
            LocalDateTime now = LocalDateTime.now();
            logPO.setCreateTime(now);
            logPO.setType(1);
            logPO.setTypeSub(1);
            logPO.setDeleted(false);
            logPO.setState(true);
            OrgCorpPO corpPO = corpService.getCorpById(id);
            logPO.setDataStr(loginUser.getName() + "删除单位：【" + corpPO.getName() + "】，成功！");
            logPOList.add(logPO);
        }
        logService.saveLogs(logPOList);
    }


}
