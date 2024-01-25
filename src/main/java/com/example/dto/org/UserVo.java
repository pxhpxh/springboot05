package com.example.dto.org;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName UserVo
 * @Description
 * @Author pxh
 * @Date 2024/1/24 15:06
 * @Version 1.0
 */
@Data
public class UserVo {

    private Long id;
    private String username;
    private String password;
    private String realname;

    private String roleName;

}
