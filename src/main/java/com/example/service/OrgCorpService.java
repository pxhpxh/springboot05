package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.OrgCorpPO;
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
public interface OrgCorpService extends IService<OrgCorpPO> {

    Pager<OrgCorpPO> getPage() throws BaseBusinessException;

    Pager<OrgCorpPO> getPage(Pager<OrgCorpPO> pager) throws BaseBusinessException;

    void saveOrEdit(OrgCorpPO orgCorpPO);

    void remove(List<Long> idList);

    boolean isExistByName(String name, Long id);

    boolean isExistByCode(String code, Long id);

    boolean isExistByShortName(String shortName, Long id);

    OrgCorpPO getCorpByName(String name);

    OrgCorpPO getCorpByShortName(String shortName);

    OrgCorpPO getCorpByCode(String code);

    OrgCorpPO getCorpById(Long id);

    long getCountCorpOfGroup(Long groupId);



}
