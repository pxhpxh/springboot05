package com.example.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pxh
 * @since 2024-01-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
