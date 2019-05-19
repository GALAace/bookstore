package com.bs.common;

/**
 * @Description: 响应状态码
 * @Auther: 杨博文
 * @Date: 2019/5/12 01:14
 */
public enum ResponseCode {
    //成功
    SUCCESS(0,"SUCCESS"),
    //错误
    ERROR(1,"ERROR"),
    //需要登陆
    NEED_LOGIN(10,"NEED_LOGIN"),
    //非法参数
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
