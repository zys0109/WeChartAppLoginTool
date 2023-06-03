package com.example.wechartapplogintool.common.util;

import com.example.wechartapplogintool.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class ExceptionUtil {


    /**
     * 未定义的所有异常
     */
    @ResponseBody
    @ExceptionHandler(value=Exception.class)
    public Result defaultErrorHandler(Exception e){
        Result responseData=new Result();
        responseData.setCode(-1);
        responseData.setMessage(e.getMessage());
        log.info(e.getMessage());
        return responseData;
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result nullErrorHandel(NullPointerException e){
        Result responseData=new Result();
        responseData.setCode(-1);
        responseData.setMessage(String.valueOf(e));
        return responseData;
    }

}
