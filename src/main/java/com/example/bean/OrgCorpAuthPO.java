package com.example.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("org_corp_auth")
public class OrgCorpAuthPO {
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`user_id`")
    private Long userId;

    @TableField("`corp_id`")
    private Long corpId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;
}
