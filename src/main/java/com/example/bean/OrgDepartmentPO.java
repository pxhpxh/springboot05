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
@TableName("org_department")
public class OrgDepartmentPO {

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`name`")
    private String name;

    @TableField("`code`")
    private String code;

    @TableField("sort")
    private Integer sort;

    @TableField("state")
    private Integer state;

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
