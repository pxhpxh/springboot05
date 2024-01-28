package com.example.controller;

import com.example.bean.OrgDepartmentPO;
import com.example.common.ResponseData;
import com.example.dto.grid.Pager;
import com.example.exception.BaseBusinessException;
import com.example.service.OrgDepartmentService;
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
@RequestMapping("/department")
public class OrgDepartmentController {

    @Autowired
    private OrgDepartmentService orgDepartmentService;

    /**
     * 获取部门列表
     *
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody Pager pager) {
        try {
            Pager orgDepartmentPOPager = orgDepartmentService.getPage(pager);
            return ResponseData.success(orgDepartmentPOPager);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }

    @RequestMapping("/saveOrEdit")
    public ResponseData saveOrEdit(@RequestBody OrgDepartmentPO orgDepartmentPO) {
        try {
            orgDepartmentService.saveOrEdit(orgDepartmentPO);
            return ResponseData.success(orgDepartmentPO);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }

    @RequestMapping("/remove")
    public ResponseData remove(@RequestBody List<Long> idList) {
        try {
            orgDepartmentService.remove(idList);
            return ResponseData.success();
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }

    /**
     * @return 前端的下拉列表获取选项
     */
    @RequestMapping("/selectOptions")
    public ResponseData list() {
        try {
            List<OrgDepartmentPO> deps = orgDepartmentService.getDepartments();
            return ResponseData.success(deps);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }

}
