package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bean.LogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pxh
 * @since 2023-8-1 11:14:19
 */
@Mapper
public interface LogDao extends BaseMapper<LogPO> {

}
