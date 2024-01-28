package com.example.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author pxh
 * @since 2023-8-1 10:33:38
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("doc_logs")
public class LogPO {

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类型
     * 1-基础档案
     */
    @TableField("type")
    private Integer type;

    /**
     * 子类型
     * 1-单位 Corp
     * 2-部门 Department
     * 3-用户 User
     */
    @TableField("type_sub")
    private Integer typeSub;

    /**
     * 日志内容
     */
    @TableField("dataStr")
    private String dataStr;

    /**
     * 状态
     */
    @TableField("state")
    private Boolean state;

    /**
     * 集团ID
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 单位ID
     */
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
