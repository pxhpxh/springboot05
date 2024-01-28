package com.example.controller;


import com.alibaba.fastjson.JSON;
import com.example.bean.User;
import com.example.common.ResponseData;
import com.example.dto.grid.Pager;
import com.example.exception.BaseBusinessException;
import com.example.service.UserService;
import com.example.util.TextEncoder;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pxh
 * @since 2024-01-19
 */
@RestController
@RequestMapping("/test/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/list2")
    public ResponseData list(@RequestBody Pager pager){
        try {
            Pager<User> bankPager = userService.getPage(pager);
            return ResponseData.success(bankPager);
        } catch (BaseBusinessException e) {
            return ResponseData.error(-1, e.getMessage());
        }
    }



}
