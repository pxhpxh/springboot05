package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.OrgRoleAuthPO;
import com.example.bean.OrgRoleResPO;
import com.example.bean.RouterPO;
import com.example.dto.RouterItem;
import com.example.dto.RouterMeta;
import com.example.exception.BaseBusinessException;
import com.example.mapper.RouterDao;
import com.example.service.LoginService;
import com.example.service.OrgRoleAuthService;
import com.example.service.OrgRoleResService;
import com.example.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssj
 * @since 2023-04-14 15:58:36
 */
@Service
public class RouterServiceImpl extends ServiceImpl<RouterDao, RouterPO> implements RouterService {
    @Autowired
    private OrgRoleAuthService orgRoleAuthService;
    @Autowired
    private OrgRoleResService orgRoleResService;
    @Autowired
    private LoginService loginService;

    /**
     * 获取当前单位所有路由
     * @return
     * @throws BaseBusinessException
     */
    @Override
    public List<RouterPO> getRouterAll() throws BaseBusinessException {
        //获取路由列表
        LambdaQueryWrapper<RouterPO> qw=new LambdaQueryWrapper<>();
        qw.eq(RouterPO::getDeleted,false);
        return this.list(qw);
    }

    /**
     * 查询当前用户所有路由
     * @return
     * @throws BaseBusinessException
     */
    @Override
    public List<RouterPO> getRouters() throws BaseBusinessException {
        return getRoutersByUID(loginService.getLoginUserId());
    }

    /**
     * 查询指定用户所有路由
     * @param uid
     * @return
     * @throws BaseBusinessException
     */
    @Override
    public List<RouterPO> getRoutersByUID(Long uid) throws BaseBusinessException {
        //获取当前人员授权角色
        List<OrgRoleAuthPO> orgRoleAuthPOs = orgRoleAuthService.getUserRoles(uid);
        List<Long> roleIds=orgRoleAuthPOs.stream().map(OrgRoleAuthPO::getRoleId).collect(Collectors.toList());

        //获取对应角色菜单授权
        List<OrgRoleResPO> orgRoleResPOS=orgRoleResService.getResByRoles(roleIds);
        List<Long> routerIds=orgRoleResPOS.stream().map(OrgRoleResPO::getRouterId).collect(Collectors.toList());
        return getRouters(routerIds);
    }

    /**
     * 根据ids获取路由对象列表
     * @param ids
     * @return
     * @throws BaseBusinessException
     */
    @Override
    public List<RouterPO> getRouters(List<Long> ids) throws BaseBusinessException {
        if(ids==null || ids.size()==0){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<RouterPO> qw=new LambdaQueryWrapper<>();
        qw.in(RouterPO::getId,ids);
        return this.list(qw);
    }

    private List<RouterPO> getRoutersByParentId(List<RouterPO> routers, RouterItem parent){
        if(routers!=null && routers.size()>0){
            List<RouterPO> menus = routers.stream().filter(router ->
                    parent.getId() == router.getParentId()
            ).collect(Collectors.toList());//本次查询出记录
            routers.removeAll(menus);//从所有路由中删除本次查询出记录
            List<RouterItem> temItems = menus.stream().map(router->{
                RouterItem vo=new RouterItem();
                vo.setId(router.getId());
                vo.setName(router.getName());
                vo.setPath(router.getPath());
                vo.setComponent(router.getComponent());
                RouterMeta meta=new RouterMeta();
                meta.setTitle(router.getTitle());
                meta.setIcon(router.getIcon());
                vo.setTitle(router.getTitle());
                vo.setIcon(router.getIcon());
                vo.setMeta(meta);
                vo.setRedirect(router.getRedirect());
                vo.setParentId(router.getParentId());
                //BeanUtils.copyProperties(router, vo);
                return vo;
            }).collect(Collectors.toList());//本次查询出记录转换为vo
            parent.setChildren(temItems);
            for(RouterItem vo:temItems){
                getRoutersByParentId(routers,vo);
            }
            return menus;
        }
        return null;
    }

    @Override
    public List<RouterItem> getAllRoutersVO() throws BaseBusinessException {
        //获取当前用户所有路由
        List<RouterPO> routers = getRouterAll();
        RouterItem root=new RouterItem();
        root.setId(0L);
        getRoutersByParentId(routers,root);
        return root.getChildren();
    }

    @Override
    public List<RouterItem> getRoutersVOByUID(Long uid) throws BaseBusinessException {
        //获取当前用户所有路由
        List<RouterPO> routers = getRouters();
        RouterItem root=new RouterItem();
        root.setId(0L);
        getRoutersByParentId(routers,root);
        return root.getChildren();
    }

    @Override
    public List<RouterItem> getRoutersVO() throws BaseBusinessException {
        return getRoutersVOByUID(loginService.getLoginUserId());
    }
}
