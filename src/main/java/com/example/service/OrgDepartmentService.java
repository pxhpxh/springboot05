package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.OrgDepartmentPO;
import com.example.dto.grid.Pager;
import com.example.exception.BaseBusinessException;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pxh
 * @since 2023-04-14 15:58:36
 */
public interface OrgDepartmentService extends IService<OrgDepartmentPO> {

    Pager getPage() throws BaseBusinessException;

    Pager<OrgDepartmentPO> getPage(Pager<OrgDepartmentPO> pager) throws BaseBusinessException;

    void saveOrEdit(OrgDepartmentPO orgDepartmentPO);

    void remove(List<Long> idList);

    boolean isExistByName(String name, Long id);

    boolean isExistByCode(String code, Long id);

    OrgDepartmentPO getByName(String name);

    OrgDepartmentPO getByCode(String code);

    OrgDepartmentPO getById(Long id);


    List<OrgDepartmentPO> getDepartments();
}
