package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.User;
import com.example.dto.grid.Pager;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pxh
 * @since 2024-01-19
 */
public interface UserService extends IService<User> {

    Pager<User> getPage(Pager pager);
}
