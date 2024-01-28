package com.example.dto.authentication;

import com.example.bean.OrgCorpPO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 8112638468118225745L;
    //用户id
    private Long id;
    //登录名
    private String loginName;
    //登录设备 PC、Mobile、wei_xin
    private String device;
    //人员姓名
    private String name;
    //人员编码
    private String code;
    //手机号码
    private String mobile;
    //电子邮箱
    private String mail;
    //人员头像
    private String avatar;
    //部门id
    private Long departmentId;
    //单位id
    private Long corpId;
    //集团id
    private Long groupId;
    //描述
    private String description;
    //角色
    private String[] roles;

    private List<OrgCorpPO> corps;

    private Long currentCorpId;
    //人员状态
    private int state;

    private LocalDateTime createTime;
}
