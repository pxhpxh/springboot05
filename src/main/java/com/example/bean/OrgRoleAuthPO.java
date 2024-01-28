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
 * @since 2023-04-23 10:10:24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("org_role_auth")
public class OrgRoleAuthPO {

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("role_id")
    private Long roleId;

    @TableField("user_id")
    private Long userId;

    @TableField("group_id")
    private Long groupId;

    @TableField("corp_id")
    private Long corpId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
