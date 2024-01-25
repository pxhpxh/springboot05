package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.util.TextEncoder;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestController
 * @Description
 * @Author pxh
 * @Date 2024/1/24 14:40
 * @Version 1.0
 */

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        return " hello Spring Boot !";
    }

    @RequestMapping("/sso/api/SSOService/GetUser")
    @ResponseBody
    public Map getUser(HttpServletRequest req, HttpServletResponse response) {
        Map<String,String> map  = new HashMap<>();
        response.setHeader("Content-Type","application/json;charset=utf8");
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(Strings.isBlank(header)){
            map.put("message","内容错误");
            return  map ;
        }
        map.put("Identity","pxh");
        return  map ;
    }

    @RequestMapping("/test1")
    public void danDianLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date date = new Date();
        String yyyyMMddHHmmss = "20120202111122";

        System.out.println("进入了登录跳转前的方法");
        String loginName = "pxh";
        //String key = UUIDUtil.getAbsUUIDLong()+"";
        loginName = TextEncoder.encode(loginName);
        loginName = URLEncoder.encode(loginName,"UTF-8");
        response.sendRedirect("127.0.0.1:8090"+ "/seeyon/login/sso?from=scjdj&ticket="+loginName+"&UserAgentFrom=pc");
    }

    @RequestMapping("/test2")
    public void test2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parameter = request.getParameter("parm1");
        Date date = new Date();
        String yyyyMMddHHmmss = "20120202111122";

        response.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("进入了登录跳转前的方法");
        String loginName = "pxh";

        //String key = UUIDUtil.getAbsUUIDLong()+"";
        loginName = TextEncoder.encode(loginName);
        loginName = URLEncoder.encode(loginName,"UTF-8");
        Map map = new HashMap<>();
        map.put("loginName","12adpd碰下");
        response.getWriter().write(JSON.toJSONString(map));
    }


}
