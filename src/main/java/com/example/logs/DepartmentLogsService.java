package com.example.logs;


import com.example.annotation.ListenEvent;
import com.example.bean.LogPO;
import com.example.bean.OrgDepartmentPO;
import com.example.bean.OrgUserPO;
import com.example.event.events.DepartmentAddEvent;
import com.example.event.events.DepartmentEditEvent;
import com.example.event.events.DepartmentRemoveEvent;
import com.example.service.LogService;
import com.example.service.OrgDepartmentService;
import com.example.util.POComparator;
import com.example.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentLogsService {

    @Autowired
    private OrgDepartmentService DepartmentService;

    @Autowired
    private LogService logService;

    @ListenEvent(event = DepartmentAddEvent.class)
    public void DepartmentAddEvent(DepartmentAddEvent event) {
        OrgDepartmentPO DepartmentPO = event.getDepartmentPO();
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
        logPO.setTypeSub(2);
        logPO.setDeleted(false);
        logPO.setState(true);
        logPO.setDataStr(loginUser.getName() + "新增部门：【" + DepartmentPO.getName() + "】，成功！");
        logService.saveLog(logPO);
    }

    @ListenEvent(event = DepartmentEditEvent.class)
    public void DepartmentEditEvent(DepartmentEditEvent event) {
        OrgDepartmentPO DepartmentPO = event.getDepartmentPO();
        OrgUserPO loginUser = event.getLoginUser();
        Long corpId = event.getCorpId();
        Long groupId = event.getGroupId();

        OrgDepartmentPO oldDepartmentPO = DepartmentService.getById(DepartmentPO.getId());

        LogPO logPO = new LogPO();
        logPO.setId(UUIDUtils.longUUID());
        logPO.setGroupId(groupId);
        logPO.setCorpId(corpId);
        LocalDateTime now = LocalDateTime.now();
        logPO.setCreateTime(now);
        logPO.setType(1);
        logPO.setTypeSub(2);
        logPO.setDeleted(false);
        logPO.setState(true);
        String changes = POComparator.getChanges(oldDepartmentPO, DepartmentPO);
        logPO.setDataStr(loginUser.getName() + "编辑部门：【" + changes + "】，成功！");
        logService.saveLog(logPO);
    }


    @ListenEvent(event = DepartmentRemoveEvent.class)
    public void DepartmentRemoveEvent(DepartmentRemoveEvent event) {
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
            logPO.setTypeSub(2);
            logPO.setDeleted(false);
            logPO.setState(true);
            OrgDepartmentPO departmentPO = DepartmentService.getById(id);
            logPO.setDataStr(loginUser.getName() + "删除部门：【" + departmentPO.getName() + "】，成功！");
            logPOList.add(logPO);
        }
        logService.saveLogs(logPOList);
    }


}
