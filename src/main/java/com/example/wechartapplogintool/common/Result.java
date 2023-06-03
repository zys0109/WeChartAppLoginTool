package com.example.wechartapplogintool.common;

import lombok.Data;

/**
 * 统一的响应结果集
 */
@Data
public class Result<T> {
    //返回状态码
    private Integer code;
    //提示信息
    private String message;
    //结果数据
    T data;
    public Result(){

    }
    public Result(ResultCode resultCode){
        this.code=resultCode.code();
        this.message=resultCode.message();
    }
    public Result(ResultCode resultCode,T data){
        this.code=resultCode.code();
        this.message=resultCode.message();
        this.data=data;
    }
    //操作成功没有数据对象
    public static Result SUCCESS(){
        return new Result(ResultCode.SUCCESS);
    }
    //操作成功有数据对象
    public static <T> Result SUCCESS(T data){
        return new Result(ResultCode.SUCCESS,data);
    }
    //操作失败
    public static Result FAIL(){
        return new Result(ResultCode.FAIL);
    }
    public  Result(int code,String message){
        this.code=code;
        this.message=message;
    }

}
