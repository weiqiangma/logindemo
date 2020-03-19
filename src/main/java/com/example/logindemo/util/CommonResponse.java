package com.example.logindemo.util;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

/**
 * 通用响应类，增加泛型接口
 * @author mwq
 * @version 2019-12-12 13:56:13 V1.1
 * @param <T> 结果result模型
 */
@Builder
@Data
@Configuration
public class CommonResponse<T> implements Serializable {

    private static final long serialVersionUID = -7367995372616638542L;

    private String code;

    private String msg;

    private T result;

    public CommonResponse(){}

    public CommonResponse(String code, String msg){
        this.code =  code;
        this.msg = msg;
    }

    public CommonResponse(String code, String msg, T result){
        this.code = code;
        this.msg = msg;
        this.result =  result;
    }
}
