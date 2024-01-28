package com.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouterItem {
    private Long id;
    private String name;
    private String path;
    private String component;
    private RouterMeta meta;
    private String title;
    private String icon;
    //private Boolean hidden;
    //private Boolean alwaysShow;

    //private Boolean noCache;
    private String redirect;
    //private String activeMenu;
    //private Boolean affix;
    private Long parentId;
    private List<RouterItem> children;
}
