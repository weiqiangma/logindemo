package com.example.logindemo.service;

import com.example.logindemo.dto.request.KaptchaCodeRequest;
import com.example.logindemo.dto.response.KaptchaCodeResponse;

public interface IKaptchaService {

    /**
     * 获取图形验证码
     * @param request
     * @return
     */
    KaptchaCodeResponse getKaptchaCode(KaptchaCodeRequest request);

    /**
     * 校验图形验证码
     * @param request
     * @return
     */
    KaptchaCodeResponse validateKaptchaCode(KaptchaCodeRequest request);
}
