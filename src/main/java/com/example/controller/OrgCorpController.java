package com.example.controller;

import com.example.bean.OrgCorpPO;
import com.example.common.ResponseData;
import com.example.dto.grid.Pager;
import com.example.exception.BaseBusinessException;
import com.example.service.OrgCorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ssj
 * @since 2023-04-14 15:58:36
 */
@RestController
@RequestMapping("/corp")
public class OrgCorpController {

    private OrgCorpService orgCorpService;

    /**
     * 获取单位列表
     *
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody Pager<OrgCorpPO> pager) {
        try {
            Pager orgCorpPOPager = orgCorpService.getPage(pager);
            return ResponseData.success(orgCorpPOPager);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }

    @RequestMapping("/saveOrEdit")
    public ResponseData saveOrEdit(@RequestBody OrgCorpPO orgCorpPO) {
        try {
            orgCorpService.saveOrEdit(orgCorpPO);
            return ResponseData.success(orgCorpPO);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }


    // TODO 通用删除、批量删除、入参IDList~~  User、Department
    @RequestMapping("/remove")
    public ResponseData remove(@RequestBody List<Long> idList) {
        try {
            orgCorpService.remove(idList);
            return ResponseData.success();
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }

    @Autowired
    public void setOrgCorpService(OrgCorpService orgCorpService) {
        this.orgCorpService = orgCorpService;
    }
}
