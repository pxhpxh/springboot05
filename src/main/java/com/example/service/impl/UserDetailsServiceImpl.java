package com.example.service.impl;


import com.example.bean.OrgUserPO;
import com.example.dto.authentication.LoginUser;
import com.example.service.OrgUserService;
import com.example.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service("webUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private OrgUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        OrgUserPO user=userService.getUserByLoginName(username);
        if (Objects.isNull(user)) {//如果用户没有查询到抛出异常
            throw new UsernameNotFoundException("用户不存在");
        }
        //TODO 查询对应的权限信息
        List<String> permissions=new ArrayList<>(Arrays.asList("group-admin","account-admin"));
        //把数据封装成UserDetails
        String uid= UUIDUtils.generateUUID();
        return new LoginUser(user,permissions,uid);
    }
}
