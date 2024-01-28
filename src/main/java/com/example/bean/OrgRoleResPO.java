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
@TableName("org_role_res")
public class OrgRoleResPO {

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("role_id")
    private Long roleId;

    @TableField("router_id")
    private Long routerId;

    @TableField("is_system")
    private Boolean system;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
