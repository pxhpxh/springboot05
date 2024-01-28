package com.example.common;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public abstract class AbstractRequest implements Serializable, Cloneable {
    private String timestamp= String.valueOf(System.currentTimeMillis());

    public String getTimestamp() {
        return timestamp;
    }

    private static final long serialVersionUID = 1803963121001416793L;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Object Clone(){
        try {
            return clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T create(JSONObject json, Class<T> cls){
        return JSONObject.parseObject(json.toJSONString(),cls);
    }
}
