package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgDepartmentPO;
import com.example.bean.OrgRoleAuthPO;
import com.example.bean.OrgUserPO;
import com.example.dto.authentication.UserResponse;
import com.example.dto.grid.Filter;
import com.example.dto.grid.Pager;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.dto.org.EditPasswordRequest;
import com.example.event.BaseEvent;
import com.example.event.EventService;
import com.example.event.EventTriggerMode;
import com.example.event.events.UserAddEvent;
import com.example.event.events.UserEditEvent;
import com.example.exception.BaseBusinessException;
import com.example.mapper.OrgUserDao;
import com.example.service.LoginService;
import com.example.service.OrgDepartmentService;
import com.example.service.OrgRoleAuthService;
import com.example.service.OrgUserService;
import com.example.util.SHA256Util;
import com.example.util.Strings;
import com.example.util.UUIDUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pxh
 * @since 2023-04-14 15:58:36
 */
@Service
public class OrgUserServiceImpl extends ServiceImpl<OrgUserDao, OrgUserPO> implements OrgUserService {
    private static Map<String, String> userTokenMap = new HashMap<>();
    private static Map<String, UserResponse> tokenMap = new HashMap<>();

    @Autowired
    private OrgRoleAuthService orgRoleAuthService;

    @Autowired
    private OrgDepartmentService orgDepartmentService;

    @Autowired
    private LoginService loginService;

    @Override
    public String createToken(UserResponse userResponse) {
        if (userTokenMap.containsKey(userResponse.getLoginName())) {
            tokenMap.remove(userTokenMap.get(userResponse.getLoginName()));
            userTokenMap.remove(userResponse.getLoginName());
        }

        String token = UUIDUtils.generateUUID().replaceAll("-", "");
        userTokenMap.put(userResponse.getLoginName(), token);
        tokenMap.put(token, userResponse);
        return token;
    }

    @Override
    public UserResponse getUserByToken(String token) {
        UserResponse user = tokenMap.get(token);
        return user;
    }

    @Override
    public void removeToken(String token) {
        UserResponse user = getUserByToken(token);
        if (user != null) {
            userTokenMap.remove(user.getLoginName());
            tokenMap.remove(token);
        }
    }

    @Override
    public void removeTokenByLoginName(String loginName) {
        String token = userTokenMap.get(loginName);
        if (Strings.isNotBlank(token)) {
            tokenMap.remove(token);
            userTokenMap.remove(loginName);
        }
    }

    @Override
    public OrgUserPO getUserByLoginName(String loginName) {
        OrgUserPO user = this.getOne(
                Wrappers.<OrgUserPO>lambdaQuery()
                        .eq(OrgUserPO::getLoginName, loginName).last("limit 1")
        );
        return user;
    }

    @Override
    public boolean isExist(String loginName, Long id) {
        LambdaQueryWrapper<OrgUserPO> lqw = Wrappers.<OrgUserPO>lambdaQuery()
                .eq(OrgUserPO::getLoginName, loginName);
        if (id != null && id != 0) {
            lqw.ne(OrgUserPO::getId, id);
        }
        lqw.last("limit 1");
        OrgUserPO user = this.getOne(lqw);
        return user != null;
    }

    @Override
    public boolean isExistCode(String code, Long id) {
        LambdaQueryWrapper<OrgUserPO> lqw = Wrappers.<OrgUserPO>lambdaQuery()
                .eq(OrgUserPO::getCode, code);
        if (id != null && id != 0) {
            lqw.ne(OrgUserPO::getId, id);
        }
        lqw.last("limit 1");
        OrgUserPO user = this.getOne(lqw);
        return user != null;
    }

    @Override
    public void saveOrEdit(AddOrEditUserResponse user) throws BaseBusinessException {
        if (user == null || Strings.isBlank(user.getLoginName())
                || Strings.isBlank(user.getName()) || Strings.isBlank(user.getPassword())
                || user.getRole().length == 0) {
            throw new BaseBusinessException("必填项未录入");
        }
        OrgUserPO loginUser = loginService.getLoginUser();
        Long currentGroupId = loginService.getCurrentGroupId();
        Long currentCorpId = loginService.getCurrentCorpId();

        BaseEvent saveOrEditEvent = null;
        OrgUserPO saveUser = null;
        boolean isNew = false;

        if (user.getId() != null && user.getId() != 0) {
            // 修改
            boolean existLoginName = isExist(user.getLoginName(), user.getId());
            if (existLoginName) {
                throw new BaseBusinessException("登录名已存在");
            }
            if (user.getCode() != null) {
                boolean existCode = isExistCode(user.getCode(), user.getId());
                if (existCode) {
                    throw new BaseBusinessException("编码已存在");
                }
            }
            saveUser = getById(user.getId());
        } else {
            // 新增
            isNew = true;

            boolean exist = isExist(user.getLoginName(), null);
            if (exist) {
                throw new BaseBusinessException("登录名已存在");
            }
            if (user.getCode() != null) {
                boolean existCode = isExistCode(user.getCode(), user.getId());
                if (existCode) {
                    throw new BaseBusinessException("编码已存在");
                }
            }
            saveUser = new OrgUserPO();
            saveUser.setId(UUIDUtils.longUUID());
        }
        if (!user.getLoginName().equals(saveUser.getLoginName()) || !"0ldPassWord~!".equalsIgnoreCase(user.getPassword())) {//需要重置密码
            String pwd = SHA256Util.encode(user.getLoginName() + "|" + user.getPassword());
            saveUser.setPassword(pwd);
            saveUser.setGroupId(currentGroupId);
            saveUser.setCreateTime(LocalDateTime.now());
            saveUser.setDeleted(false);
        }
        saveUser.setLoginName(user.getLoginName());
        saveUser.setName(user.getName());
        saveUser.setCode(user.getCode());
        saveUser.setCorpId(currentCorpId);
        saveUser.setDepartmentId(user.getDepartmentId());
        saveUser.setMobile(user.getMobile());
        saveUser.setMail(user.getMail());
        saveUser.setState(user.getState());

        boolean ret = false;
        if (isNew) {
            // 新增事件 before
            saveOrEditEvent = new UserAddEvent();
            ((UserAddEvent) saveOrEditEvent).setLoginUser(loginUser);
            ((UserAddEvent) saveOrEditEvent).setGroupId(currentGroupId);
            ((UserAddEvent) saveOrEditEvent).setCorpId(currentCorpId);
            ((UserAddEvent) saveOrEditEvent).setUserPO(saveUser);
            try {
                EventService.trigger(saveOrEditEvent, EventTriggerMode.before);
            } catch (Exception e) {
                throw new BaseBusinessException("新增用户事件[before]发生错误，请联系管理员处理");
            }

            // 新增事件执行
            ret = this.save(saveUser);
            user.setId(saveUser.getId());
            orgRoleAuthService.saveOrEdit(user);

            // 新增事件 before
            if (ret) {
                try {
                    EventService.trigger(saveOrEditEvent, EventTriggerMode.after);
                } catch (Exception e) {
                    throw new RuntimeException("新增用户事件[after]发生错误，请联系管理员！");
                }
            } else {
                throw new BaseBusinessException("新增用户时发生异常，请联系管理员！");
            }
        } else {
            // 编辑事件 before
            saveOrEditEvent = new UserEditEvent();
            ((UserEditEvent) saveOrEditEvent).setLoginUser(loginUser);
            ((UserEditEvent) saveOrEditEvent).setGroupId(currentGroupId);
            ((UserEditEvent) saveOrEditEvent).setCorpId(currentCorpId);
            ((UserEditEvent) saveOrEditEvent).setUserPO(saveUser);
            try {
                EventService.trigger(saveOrEditEvent, EventTriggerMode.before);
            } catch (Exception e) {
                throw new BaseBusinessException("编辑用户事件[before]发生错误，请联系管理员处理");
            }

            ret = this.updateById(saveUser);
            user.setId(saveUser.getId());
            orgRoleAuthService.saveOrEdit(user);

            if (ret) {
                // 编辑事件 after
                try {
                    EventService.trigger(saveOrEditEvent, EventTriggerMode.after);
                } catch (Exception e) {
                    throw new RuntimeException("编辑用户事件[after]发生错误，请联系管理员！");
                }
            } else {
                throw new BaseBusinessException("编辑用户时发生异常，请联系管理员！");
            }
        }
    }

    @Override
    public void editPassword(EditPasswordRequest editPassword) throws BaseBusinessException {
        OrgUserPO user = this.getById(editPassword.getId());
        if (user == null) {
            throw new BaseBusinessException("用户不存在");
        }
        String oldPwd = SHA256Util.encode(user.getLoginName() + "|" + editPassword.getOldPassword());
        if (!user.getPassword().equals(oldPwd)) {
            throw new BaseBusinessException("原密码不正确");
        }
        String newPwd_db = SHA256Util.encode(user.getLoginName() + "|" + editPassword.getNewPassword());
        user.setPassword(newPwd_db);
        this.updateById(user);
    }

    @Override
    public Pager getPage() throws BaseBusinessException {
        return this.getPage(null);
    }

    @Override
    public Pager getPage(Pager<OrgUserPO> pager) throws BaseBusinessException {
        boolean isPage = true;
        // pager为空 即非页面获取列表
        if (pager == null) {
            isPage = false;
            pager = new Pager<>();
        }
        Long currentGroupId = loginService.getCurrentGroupId();
        Long currentCorpId = loginService.getCurrentCorpId();
        LambdaQueryWrapper<OrgUserPO> lqw = new LambdaQueryWrapper<>();
        if (isPage) {
            List<Filter> filters = pager.getFilters();
            for (Filter filter : filters) {
                if ("name".equals(filter.getFieldName())) {
                    lqw.eq(OrgUserPO::getName, filter.getParValue1());
                }
                if ("loginName".equals(filter.getFieldName())) {
                    lqw.eq(OrgUserPO::getLoginName, filter.getParValue1());
                }
                if ("code".equals(filter.getFieldName())) {
                    lqw.eq(OrgUserPO::getCode, filter.getParValue1());
                }
                if ("mobile".equals(filter.getFieldName())) {
                    lqw.eq(OrgUserPO::getMobile, filter.getParValue1());
                }
                if ("mail".equals(filter.getFieldName())) {
                    lqw.eq(OrgUserPO::getMail, filter.getParValue1());
                }
                if ("avatar".equals(filter.getFieldName())) {
                    lqw.eq(OrgUserPO::getAvatar, filter.getParValue1());
                }
            }
        }

        lqw.eq(OrgUserPO::getGroupId, currentGroupId);
        lqw.eq(OrgUserPO::getCorpId, currentCorpId);

        lqw.orderByDesc(OrgUserPO::getCreateTime);
        Pager<OrgUserPO> pagePO = this.page(pager, lqw);

        if (isPage){
            List<AddOrEditUserResponse> list = pagePO.getRecords().stream().map(po -> {
                AddOrEditUserResponse vo = new AddOrEditUserResponse();
                BeanUtils.copyProperties(po, vo);
                vo.setPassword("0ldPassWord~!");
                if (po.getId() != null) {
                    List<OrgRoleAuthPO> roleAuthPOList = orgRoleAuthService.getUserRoles(vo.getId(), currentGroupId, currentCorpId);
                    String[] roleIdArray = roleAuthPOList.stream()
                            .map(OrgRoleAuthPO::getRoleId)
                            .map(String::valueOf)
                            .toArray(String[]::new);
                    vo.setRole(roleIdArray);
                    OrgDepartmentPO depById = orgDepartmentService.getById(vo.getDepartmentId());
                    vo.setDepartmentName(depById.getName());
                }
                return vo;
            }).collect(Collectors.toList());
            Pager<AddOrEditUserResponse> pageVO = new Pager<>();
            pageVO.setFlipInfo(pagePO.getFlipInfo());
            pageVO.setPageData(list);
            return pageVO;
        }
        return pagePO;
    }

}
