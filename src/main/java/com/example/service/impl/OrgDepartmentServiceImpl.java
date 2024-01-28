package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgDepartmentPO;
import com.example.bean.OrgUserPO;
import com.example.dto.grid.Filter;
import com.example.dto.grid.Pager;
import com.example.event.BaseEvent;
import com.example.event.EventService;
import com.example.event.EventTriggerMode;
import com.example.event.events.DepartmentAddEvent;
import com.example.event.events.DepartmentEditEvent;
import com.example.event.events.DepartmentRemoveEvent;
import com.example.exception.BaseBusinessException;
import com.example.mapper.OrgDepartmentDao;
import com.example.service.LoginService;
import com.example.service.OrgDepartmentService;
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
public class OrgDepartmentServiceImpl extends ServiceImpl<OrgDepartmentDao, OrgDepartmentPO> implements OrgDepartmentService {

    @Autowired
    private LoginService loginService;

    @Override
    public Pager getPage() throws BaseBusinessException {
        return this.getPage(null);
    }

    @Override
    public Pager getPage(Pager<OrgDepartmentPO> pager) throws BaseBusinessException {
        boolean isPage = true;
        // pager为空 即非页面获取列表
        if (pager == null) {
            isPage = false;
            pager = new Pager<>();
        }

        LambdaQueryWrapper<OrgDepartmentPO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrgDepartmentPO::getGroupId, loginService.getCurrentGroupId());
        lqw.eq(OrgDepartmentPO::getCorpId, loginService.getCurrentCorpId());

        if (isPage) {
            List<Filter> filters = pager.getFilters();
            for (Filter filter : filters) {
                if ("name".equals(filter.getFieldName())) {
                    lqw.eq(OrgDepartmentPO::getName, filter.getParValue1());
                }
                if ("code".equals(filter.getFieldName())) {
                    lqw.eq(OrgDepartmentPO::getCode, filter.getParValue1());
                }
            }
        }

        lqw.orderByDesc(OrgDepartmentPO::getCreateTime);
        return this.page(pager, lqw);
    }

    @Override
    public void saveOrEdit(OrgDepartmentPO orgDepartmentPO) {
        if (orgDepartmentPO == null
                || Strings.isBlank(orgDepartmentPO.getName())
                || Strings.isBlank(orgDepartmentPO.getCode())) {
            throw new BaseBusinessException("必填项未录入");
        }

        OrgUserPO loginUser = loginService.getLoginUser();
        Long currentGroupId = loginService.getCurrentGroupId();
        Long currentCorpId = loginService.getCurrentCorpId();

        BaseEvent saveOrEditEvent = null;

        OrgDepartmentPO saveDep = null;
        boolean isNew = false;
        if (orgDepartmentPO.getId() != null && orgDepartmentPO.getId() != 0L) {
            // 编辑
            boolean existName = isExistByName(orgDepartmentPO.getName(), orgDepartmentPO.getId());
            if (existName) {
                throw new BaseBusinessException("名称已存在");
            }

            if (orgDepartmentPO.getCode() != null) {
                boolean existCode = isExistByCode(orgDepartmentPO.getCode(), orgDepartmentPO.getId());
                if (existCode) {
                    throw new BaseBusinessException("编码已存在");
                }
            }
            saveDep = orgDepartmentPO;
        } else {
            // 新增
            isNew = true;

            boolean existName = isExistByName(orgDepartmentPO.getName(), null);
            if (existName) {
                throw new BaseBusinessException("名称已存在");
            }
            if (orgDepartmentPO.getCode() != null) {
                boolean existCode = isExistByCode(orgDepartmentPO.getCode(), null);
                if (existCode) {
                    throw new BaseBusinessException("编码已存在");
                }
            }
            saveDep = new OrgDepartmentPO();
            saveDep.setId(UUIDUtils.longUUID());
            saveDep.setGroupId(currentGroupId);
            saveDep.setCreateTime(LocalDateTime.now());
            saveDep.setDeleted(false);

        }
        if (orgDepartmentPO.getCode() != null) {
            saveDep.setCode(orgDepartmentPO.getCode());
        }
        if (orgDepartmentPO.getSort() != null) {
            saveDep.setSort(orgDepartmentPO.getSort());
        }
        saveDep.setName(orgDepartmentPO.getName());
        saveDep.setState(orgDepartmentPO.getState());
        saveDep.setCorpId(currentCorpId);

        boolean ret = false;
        if (isNew) {
            // 新增部门事件 before
            saveOrEditEvent = new DepartmentAddEvent();
            ((DepartmentAddEvent) saveOrEditEvent).setLoginUser(loginUser);
            ((DepartmentAddEvent) saveOrEditEvent).setGroupId(currentGroupId);
            ((DepartmentAddEvent) saveOrEditEvent).setCorpId(currentCorpId);
            ((DepartmentAddEvent) saveOrEditEvent).setDepartmentPO(orgDepartmentPO);
            try {
                EventService.trigger(saveOrEditEvent, EventTriggerMode.before);
            } catch (Exception e) {
                throw new BaseBusinessException("新增部门事件[before]发生错误，请联系管理员处理");
            }

            // 新增部门执行
            ret = this.save(saveDep);

            if (ret) {
                // 新增部门事件 after
                try {
                    EventService.trigger(saveOrEditEvent, EventTriggerMode.after);
                } catch (Exception e) {
                    throw new BaseBusinessException("新增部门事件[after]发生错误，请联系管理员！");
                }
            } else {
                throw new BaseBusinessException("新增部门时发生异常，请联系管理员！");
            }
        } else {
            // 编辑部门事件 before
            saveOrEditEvent = new DepartmentEditEvent();
            ((DepartmentEditEvent) saveOrEditEvent).setLoginUser(loginUser);
            ((DepartmentEditEvent) saveOrEditEvent).setGroupId(currentGroupId);
            ((DepartmentEditEvent) saveOrEditEvent).setCorpId(currentCorpId);
            ((DepartmentEditEvent) saveOrEditEvent).setDepartmentPO(orgDepartmentPO);
            try {
                EventService.trigger(saveOrEditEvent, EventTriggerMode.before);
            } catch (Exception e) {
                throw new BaseBusinessException("编辑部门事件[before]发生错误，请联系管理员处理");
            }

            // 编辑部门执行
            ret = this.updateById(saveDep);

            if (ret) {
                // 编辑部门事件 after
                try {
                    EventService.trigger(saveOrEditEvent, EventTriggerMode.after);
                } catch (Exception e) {
                    throw new BaseBusinessException("编辑部门事件[after]发生错误，请联系管理员！");
                }
            } else {
                throw new BaseBusinessException("编辑部门时发生异常，请联系管理员！");
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
        LambdaQueryWrapper<OrgDepartmentPO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrgDepartmentPO::getGroupId, currentGroupId);
        lqw.eq(OrgDepartmentPO::getCorpId, currentCorpId);
        lqw.in(OrgDepartmentPO::getId, idList);

        // 删除部门事件 before
        DepartmentRemoveEvent departmentRemoveEvent = new DepartmentRemoveEvent();
        departmentRemoveEvent.setLoginUser(loginUser);
        departmentRemoveEvent.setGroupId(currentGroupId);
        departmentRemoveEvent.setCorpId(currentCorpId);
        departmentRemoveEvent.setIdList(idList);
        try {
            EventService.trigger(departmentRemoveEvent, EventTriggerMode.before);
        } catch (Exception e) {
            throw new BaseBusinessException("删除部门事件[before]发生错误，请联系管理员处理");
        }

        // 删除部门执行
        boolean ret = this.remove(lqw);

        if (ret) {
            // 删除部门事件 after
            try {
                EventService.trigger(departmentRemoveEvent, EventTriggerMode.after);
            } catch (Exception e) {
                throw new BaseBusinessException("删除部门事件[after]发生错误，请联系管理员处理");
            }
        } else {
            throw new BaseBusinessException("编辑部门时发生异常，请联系管理员！");
        }
    }


    @Override
    public boolean isExistByName(String name, Long id) {
        LambdaQueryWrapper<OrgDepartmentPO> lqw = Wrappers.<OrgDepartmentPO>lambdaQuery()
                .eq(OrgDepartmentPO::getName, name);
        if (id != null && id != 0) {
            lqw.ne(OrgDepartmentPO::getId, id);
        }
        lqw.last("limit 1");
        OrgDepartmentPO departmentPO = this.getOne(lqw);
        return departmentPO != null;
    }

    @Override
    public boolean isExistByCode(String code, Long id) {
        LambdaQueryWrapper<OrgDepartmentPO> lqw = Wrappers.<OrgDepartmentPO>lambdaQuery()
                .eq(OrgDepartmentPO::getCode, code);
        if (id != null && id != 0) {
            lqw.ne(OrgDepartmentPO::getId, id);
        }
        lqw.last("limit 1");
        OrgDepartmentPO departmentPO = this.getOne(lqw);
        return departmentPO != null;
    }

    @Override
    public OrgDepartmentPO getByName(String name) {
        OrgDepartmentPO po = this.getOne(
                Wrappers.<OrgDepartmentPO>lambdaQuery()
                        .eq(OrgDepartmentPO::getName, name).last("limit 1")
        );
        return po;
    }

    @Override
    public OrgDepartmentPO getByCode(String code) {
        OrgDepartmentPO po = this.getOne(
                Wrappers.<OrgDepartmentPO>lambdaQuery()
                        .eq(OrgDepartmentPO::getCode, code).last("limit 1")
        );
        return po;
    }

    @Override
    public OrgDepartmentPO getById(Long id) {
        OrgDepartmentPO po = this.getOne(
                Wrappers.<OrgDepartmentPO>lambdaQuery()
                        .eq(OrgDepartmentPO::getId, id).last("limit 1")
        );
        return po;
    }

    /**
     * 获取当前集团单位下的所有部门
     *
     * @return 当前集团单位下的所有部门
     * @throws BaseBusinessException
     */
    @Override
    public List<OrgDepartmentPO> getDepartments() throws BaseBusinessException {
        LambdaQueryWrapper<OrgDepartmentPO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrgDepartmentPO::getDeleted, false)
                .eq(OrgDepartmentPO::getGroupId, loginService.getCurrentGroupId())
                .eq(OrgDepartmentPO::getCorpId, loginService.getCurrentCorpId());
        List<OrgDepartmentPO> departmentPOList = null;
        try {
            departmentPOList = this.list(lqw);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseBusinessException("获取单位角色出错");
        }
        return departmentPOList;
    }

}
