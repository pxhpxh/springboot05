package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.RouterPO;
import com.example.dto.RouterItem;
import com.example.exception.BaseBusinessException;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssj
 * @since 2023-04-14 15:58:36
 */
public interface RouterService extends IService<RouterPO> {
    //获取全部路由列表（管理后台），支持分页
    List<RouterPO> getRouterAll() throws BaseBusinessException;
    //获取当前用户路由列表（前台）
    List<RouterPO> getRouters() throws BaseBusinessException;
    //获取指定用户路由列表（前台）
    List<RouterPO> getRoutersByUID(Long uid) throws BaseBusinessException;
    List<RouterPO> getRouters(List<Long> ids) throws BaseBusinessException;
    List<RouterItem> getRoutersVOByUID(Long uid) throws BaseBusinessException;
    List<RouterItem> getRoutersVO() throws BaseBusinessException;
    List<RouterItem> getAllRoutersVO() throws BaseBusinessException;
}
