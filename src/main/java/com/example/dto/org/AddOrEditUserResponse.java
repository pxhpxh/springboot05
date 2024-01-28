package com.example.dto.org;

import com.example.common.AbstractRequest;
import lombok.Data;

@Data
public class AddOrEditUserResponse extends AbstractRequest {

    private Long id;
    private Long corpId;
    private Long groupId;
    private Long departmentId;
    private String departmentName;
    private String loginName;
    private String password;
    private String name;
    private String code;
    private String mobile;
    private String mail;
    private String avatar;
    private String introduction;
    private String description;
    private String[] role;
    private Integer state;
}
