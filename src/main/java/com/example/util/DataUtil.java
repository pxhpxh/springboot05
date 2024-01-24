package com.example.util;

import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;

/**
 * @ClassName DataUtil
 * @Description 数据处理的基本工具类
 * @Author pxh
 * @Date 2024/1/24 11:33
 * @Version 1.0
 */

public class DataUtil {

    private  static   Boolean  isBlank(Object  obj){
        if(obj == null){
            return  true;
        }
        if("".equals(obj+"")){
            return  true;
        }
        return  false;
    }


    private static Boolean isNotBlank(Object  obj){
        return !isBlank(obj);
    }

    private  static  Boolean  isBlankOr0(Object  obj){
        if(obj == null){
            return  true;
        }
        if("".equals(obj+"")){
            return  true;
        }
        if(NumberUtils.isNumber(obj+"")){
            if(BigDecimal.ZERO.compareTo(new BigDecimal(obj+"")) == 0){
                return true;
            }
        }
        return  false;
    }

    private static Boolean isNotBlankAnd0(Object  obj){
        return !isBlankOr0(obj);
    }
}
