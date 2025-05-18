package com.earlywarning.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByError(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }

    public static <T> ServerResponse<T> badArgument() {
        return createByErrorCodeMessage(401, "参数不对");
    }

    public static <T> ServerResponse<T> badArgumentValue() {
        return createByErrorCodeMessage(402, "参数值不对");
    }

    public static <T> ServerResponse<T> unlogin() {
        return createByErrorCodeMessage(501, "请登录");
    }

    public static <T> ServerResponse<T> serious() {
        return createByErrorCodeMessage(502, "系统内部错误");
    }

    public static <T> ServerResponse<T> unsupport() {
        return createByErrorCodeMessage(503, "业务不支持");
    }

    public static <T> ServerResponse<T> updatedDateExpired() {
        return createByErrorCodeMessage(504, "更新数据已经失效");
    }

    public static <T> ServerResponse<T> updatedDataFailed() {
        return createByErrorCodeMessage(505, "更新数据失败");
    }

    public static <T> ServerResponse<T> unauthz() {
        return createByErrorCodeMessage(506, "无操作权限");
    }

    public static <T> ServerResponse<T> checkFailed() {
        return createByErrorCodeMessage(507, "user check failed");
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}
