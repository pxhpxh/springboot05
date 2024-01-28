package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgCorpPO;
import com.example.bean.OrgGroupPO;
import com.example.bean.OrgUserPO;
import com.example.dto.grid.Filter;
import com.example.dto.grid.Pager;
import com.example.event.BaseEvent;
import com.example.event.EventService;
import com.example.event.EventTriggerMode;
import com.example.event.events.CorpAddEvent;
import com.example.event.events.CorpEditEvent;
import com.example.event.events.CorpRemoveEvent;
import com.example.exception.BaseBusinessException;
import com.example.mapper.OrgCorpDao;
import com.example.service.LoginService;
import com.example.service.OrgCorpService;
import com.example.service.OrgGroupService;
import com.example.util.UUIDUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pxh
 * @since 2023-04-14 15:58:36
 */
@Service
public class OrgCorpServiceImpl extends ServiceImpl<OrgCorpDao, OrgCorpPO> implements OrgCorpService {

    @Autowired
    private LoginService loginService;

    @Autowired
    private OrgGroupService orgGroupService;

    @Override
    public Pager getPage() throws BaseBusinessException {
        return this.getPage(null);
    }

    @Override
    public Pager getPage(Pager<OrgCorpPO> pager) throws BaseBusinessException {
        boolean isPage = true;
        // pager为空 即非页面获取列表
        if (pager == null) {
            isPage = false;
            pager = new Pager<>();
        }

        LambdaQueryWrapper<OrgCorpPO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrgCorpPO::getGroupId, loginService.getCurrentGroupId());
        if (isPage) {
            List<Filter> filters = pager.getFilters();
            for (Filter filter : filters) {
                if ("name".equals(filter.getFieldName())) {
                    lqw.eq(OrgCorpPO::getName, filter.getParValue1());
                }
                if ("code".equals(filter.getFieldName())) {
                    lqw.eq(OrgCorpPO::getCode, filter.getParValue1());
                }
                if ("shortName".equals(filter.getFieldName())) {
                    lqw.eq(OrgCorpPO::getShortName, filter.getParValue1());
                }
            }
        }

        lqw.orderByDesc(OrgCorpPO::getCreateTime);
        return this.page(pager, lqw);
    }

    @Override
    public void saveOrEdit(OrgCorpPO orgCorpPO) throws BaseBusinessException {
        if (orgCorpPO == null
                || Strings.isBlank(orgCorpPO.getName())
                || Strings.isBlank(orgCorpPO.getShortName())) {
            throw new BaseBusinessException("必填项未录入");
        }
        OrgUserPO loginUser = loginService.getLoginUser();
        Long currentGroupId = loginService.getCurrentGroupId();
        Long currentCorpId = loginService.getCurrentCorpId();

        BaseEvent saveOrEditEvent = null;
        OrgCorpPO saveDep = null;
        boolean isNew = false;

        if (orgCorpPO.getId() != null && orgCorpPO.getId() != 0L) {
            // 编辑
            boolean existName = isExistByName(orgCorpPO.getName(), orgCorpPO.getId());
            if (existName) {
                throw new BaseBusinessException("名称已存在");
            }

            boolean existShortName = isExistByShortName(orgCorpPO.getShortName(), orgCorpPO.getId());
            if (existShortName) {
                throw new BaseBusinessException("简称已存在");
            }

            if (orgCorpPO.getCode() != null) {
                boolean existCode = isExistByCode(orgCorpPO.getCode(), orgCorpPO.getId());
                if (existCode) {
                    throw new BaseBusinessException("编码已存在");
                }
            }
            saveDep = orgCorpPO;
        } else {
            // 新增
            isNew = true;

            OrgGroupPO groupPO = orgGroupService.getById(currentGroupId);
            Integer unitCount = groupPO.getUnitCount();
            long countCorpOfGroup = this.getCountCorpOfGroup(currentGroupId);
            if (countCorpOfGroup >= unitCount) {
                throw new BaseBusinessException("已达最大单位数量");
            }

            boolean existName = isExistByName(orgCorpPO.getName(), null);
            if (existName) {
                throw new BaseBusinessException("名称已存在");
            }

            boolean existShortName = isExistByShortName(orgCorpPO.getShortName(), null);
            if (existShortName) {
                throw new BaseBusinessException("简称已存在");
            }

            if (orgCorpPO.getCode() != null) {
                boolean existCode = isExistByCode(orgCorpPO.getCode(), null);
                if (existCode) {
                    throw new BaseBusinessException("编码已存在");
                }
            }
            saveDep = new OrgCorpPO();
            saveDep.setId(UUIDUtils.longUUID());
            saveDep.setCreateTime(LocalDateTime.now());
            saveDep.setGroupId(currentGroupId);
            saveDep.setDeleted(false);
        }
        if (orgCorpPO.getCode() != null) {
            saveDep.setCode(orgCorpPO.getCode());
        }
        saveDep.setName(orgCorpPO.getName());
        saveDep.setShortName(orgCorpPO.getShortName());

        boolean ret = false;
        if (isNew) {
            // 新增事件 before
            saveOrEditEvent = new CorpAddEvent();
            ((CorpAddEvent) saveOrEditEvent).setLoginUser(loginUser);
            ((CorpAddEvent) saveOrEditEvent).setGroupId(currentGroupId);
            ((CorpAddEvent) saveOrEditEvent).setCorpId(currentCorpId);
            ((CorpAddEvent) saveOrEditEvent).setCorpPO(orgCorpPO);
            try {
                EventService.trigger(saveOrEditEvent, EventTriggerMode.before);
            } catch (Exception e) {
                throw new BaseBusinessException("新增单位事件[before]发生错误，请联系管理员处理");
            }

            // 执行新增
            ret = this.save(saveDep);

            // 新增事件 before
            if (ret) {
                try {
                    EventService.trigger(saveOrEditEvent, EventTriggerMode.after);
                } catch (Exception e) {
                    throw new RuntimeException("新增单位事件[after]发生错误，请联系管理员！");
                }
            } else {
                throw new BaseBusinessException("新增单位时发生异常，请联系管理员！");
            }
        } else {
            // 编辑事件 before
            saveOrEditEvent = new CorpEditEvent();
            ((CorpEditEvent) saveOrEditEvent).setLoginUser(loginUser);
            ((CorpEditEvent) saveOrEditEvent).setGroupId(currentGroupId);
            ((CorpEditEvent) saveOrEditEvent).setCorpId(currentCorpId);
            ((CorpEditEvent) saveOrEditEvent).setCorpPO(orgCorpPO);
            try {
                EventService.trigger(saveOrEditEvent, EventTriggerMode.before);
            } catch (Exception e) {
                throw new BaseBusinessException("编辑单位事件[before]发生错误，请联系管理员处理");
            }

            // 执行编辑
            ret = this.updateById(saveDep);

            if (ret) {
                // 编辑事件 after
                try {
                    EventService.trigger(saveOrEditEvent, EventTriggerMode.after);
                } catch (Exception e) {
                    throw new RuntimeException("编辑单位事件[after]发生错误，请联系管理员！");
                }
            } else {
                throw new BaseBusinessException("编辑单位时发生异常，请联系管理员！");
            }
        }

    }

    @Override
    public void remove(List<Long> idList) {
        OrgUserPO loginUser = loginService.getLoginUser();
        Long currentGroupId = loginService.getCurrentGroupId();
        Long currentCorpId = loginService.getCurrentCorpId();

        if (idList.size() == 0) {
            throw new BaseBusinessException("未获取到数据");
        }
        LambdaQueryWrapper<OrgCorpPO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrgCorpPO::getGroupId, currentGroupId);
        lqw.in(OrgCorpPO::getId, idList);

        // 删除单位事件 before
        CorpRemoveEvent corpRemoveEvent = new CorpRemoveEvent();
        corpRemoveEvent.setLoginUser(loginUser);
        corpRemoveEvent.setGroupId(currentGroupId);
        corpRemoveEvent.setCorpId(currentCorpId);
        corpRemoveEvent.setIdList(idList);
        try {
            EventService.trigger(corpRemoveEvent, EventTriggerMode.before);
        } catch (Exception e) {
            throw new RuntimeException("删除单位事件[before]发生错误，请联系管理员处理");
        }

        // 执行删除单位
        boolean ret = this.remove(lqw);

        // 删除单位事件 after
        if (ret) {
            try {
                EventService.trigger(corpRemoveEvent, EventTriggerMode.after);
            } catch (Exception e) {
                throw new RuntimeException("删除单位事件[after]发生错误，请联系管理员处理");
            }
        } else {
            throw new BaseBusinessException("编辑部门时发生异常，请联系管理员！");
        }

    }

    @Override
    public boolean isExistByName(String name, Long id) {
        LambdaQueryWrapper<OrgCorpPO> lqw = Wrappers.<OrgCorpPO>lambdaQuery()
                .eq(OrgCorpPO::getName, name);
        if (id != null && id != 0) {
            lqw.ne(OrgCorpPO::getId, id);
        }
        lqw.last("limit 1");
        OrgCorpPO corpPO = this.getOne(lqw);
        return corpPO != null;
    }

    @Override
    public boolean isExistByCode(String code, Long id) {
        LambdaQueryWrapper<OrgCorpPO> lqw = Wrappers.<OrgCorpPO>lambdaQuery()
                .eq(OrgCorpPO::getCode, code);
        if (id != null && id != 0) {
            lqw.ne(OrgCorpPO::getId, id);
        }
        lqw.last("limit 1");
        OrgCorpPO corpPO = this.getOne(lqw);
        return corpPO != null;
    }

    @Override
    public boolean isExistByShortName(String shortName, Long id) {
        LambdaQueryWrapper<OrgCorpPO> lqw = Wrappers.<OrgCorpPO>lambdaQuery()
                .eq(OrgCorpPO::getShortName, shortName);
        if (id != null && id != 0) {
            lqw.ne(OrgCorpPO::getId, id);
        }
        lqw.last("limit 1");
        OrgCorpPO corpPO = this.getOne(lqw);
        return corpPO != null;
    }

    @Override
    public OrgCorpPO getCorpByName(String name) {
        OrgCorpPO corpPO = this.getOne(
                Wrappers.<OrgCorpPO>lambdaQuery()
                        .eq(OrgCorpPO::getName, name).last("limit 1")
        );
        return corpPO;
    }

    @Override
    public OrgCorpPO getCorpByShortName(String shortName) {
        OrgCorpPO corpPO = this.getOne(
                Wrappers.<OrgCorpPO>lambdaQuery()
                        .eq(OrgCorpPO::getShortName, shortName).last("limit 1")
        );
        return corpPO;
    }

    @Override
    public OrgCorpPO getCorpByCode(String code) {
        OrgCorpPO corpPO = this.getOne(
                Wrappers.<OrgCorpPO>lambdaQuery()
                        .eq(OrgCorpPO::getCode, code).last("limit 1")
        );
        return corpPO;
    }

    @Override
    public OrgCorpPO getCorpById(Long id) {
        OrgCorpPO corpPO = this.getOne(
                Wrappers.<OrgCorpPO>lambdaQuery()
                        .eq(OrgCorpPO::getId, id).last("limit 1")
        );
        return corpPO;
    }

    @Override
    public long getCountCorpOfGroup(Long groupId) {
        LambdaQueryWrapper<OrgCorpPO> qw = new LambdaQueryWrapper<>();
        qw.eq(OrgCorpPO::getGroupId, groupId);
        return this.count(qw);
    }

}
