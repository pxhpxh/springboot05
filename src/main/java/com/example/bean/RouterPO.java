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
 * @author ssj
 * @since 2023-04-14 15:58:36
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("router")
public class RouterPO {

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`name`")
    private String name;

    @TableField("title")
    private String title;

    @TableField("icon")
    private String icon;

    @TableField("`path`")
    private String path;

    @TableField("hidden")
    private Boolean hidden;

    @TableField("always_show")
    private Boolean alwaysShow;

    @TableField("`component`")
    private String component;

    @TableField("no_cache")
    private Boolean noCache;

    @TableField("redirect")
    private String redirect;

    @TableField("active_menu")
    private String activeMenu;

    @TableField("affix")
    private Boolean affix;

    @TableField("parent_id")
    private Long parentId;

    @TableField("state")
    private Integer state;

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
