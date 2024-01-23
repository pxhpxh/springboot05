package com.example.common;


import java.io.Serializable;

public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = -9056247361336109491L;

    //请求状态
    private boolean success;
    //消息
    private String message;
    //状态码
    private int code;

    //返回数据
    private T data;

    /**
     * 时间戳
     */
    private String timestamp = String.valueOf(System.currentTimeMillis());

    /**
     * 根据数据对象模板创建正确返回对象
     */
    public static ResponseData success(){
        return create(true,0,null,null);
    }

    /**
     * 根据数据对象模板创建正确返回对象
     * @param data 数据
     * @param <T> 数据对象模板
     * @return 返回正确对象
     */
    public static <T> ResponseData success(T data){
        return create(true,0,data,null);
    }

    /**
     * 根据消息创建正确返回对象
     * @param message 消息
     * @return 返回正确对象
     */
    public static ResponseData success(String message){
        return create(true,0,null,message);
    }

    /**
     * 根据数据对象和消息创建正确返回对象
     * @param data 数据
     * @param message 消息
     * @param <T> 数据对象模板
     * @return 返回正确对象
     */
    public static <T> ResponseData success(T data, String message){
        return create(true,0,data,message);
    }

    /**
     * 根据状态码、数据创建正确返回对象
     * @param code 状态码
     * @param data 数据
     * @param <T> 数据模板
     * @return 返回正确对象
     */
    public static <T> ResponseData success(int code, T data){
        return create(true,code,data,null);
    }

    /**
     * 根据状态码、数据、消息创建正确返回对象
     * @param code 状态码
     * @param data 数据
     * @param message 消息
     * @param <T> 数据模板
     * @return 返回正确对象
     */
    public static <T> ResponseData success(int code, T data, String message){
        return create(true,code,data,message);
    }

    /**
     * 根据错误消息返回错误返回对象
     * @param message 错误消息
     * @return 返回错误对象
     */
    public static ResponseData error(String message){
        return create(false,-1,null,message);
    }

    /**
     * 根据错误编码及错误消息返回错误返回对象
     * @param code 错误编码
     * @param message 错误消息
     * @return 返回错误对象
     */
    public static ResponseData error(int code, String message){
        return create(false,code,null,message);
    }


    /**
     * 创建返回对象
     * @param success 请求状态
     * @param code 状态码
     * @param data 数据
     * @param message 返回消息
     * @param <T> 数据模板
     * @return 返回请求数据
     */
    public static <T> ResponseData create(boolean success, int code, T data, String message){
        ResponseData<T> responseData=new ResponseData<>();
        responseData.setSuccess(success);
        responseData.setCode(code);
        responseData.setData(data);
        responseData.setMessage(message);
        return responseData;
    }

    private ResponseData(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
