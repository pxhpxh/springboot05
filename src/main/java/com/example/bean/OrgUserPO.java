package com.example.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author pxh
 * @since 2023-04-14 15:58:36
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("org_user")
public class OrgUserPO {

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("login_name")
    private String loginName;

    @TableField("`password`")
    private String password;

    @TableField("`name`")
    private String name;

    @TableField("`code`")
    private String code;

    @TableField("mobile")
    private String mobile;

    @TableField("mail")
    private String mail;

    @TableField("avatar")
    private String avatar;

    @TableField("state")
    private Integer state;

    @TableField("department_id")
    private Long departmentId;

    @TableField("corp_id")
    private Long corpId;

    @TableField("group_id")
    private Long groupId;

    @TableField("`description`")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
