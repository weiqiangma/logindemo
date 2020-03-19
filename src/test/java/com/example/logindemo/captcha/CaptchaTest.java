package com.example.logindemo.captcha;

import com.example.logindemo.dto.request.KaptchaCodeRequest;
import com.example.logindemo.dto.response.KaptchaCodeResponse;
import com.example.logindemo.service.IKaptchaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaptchaTest {

    @Autowired
    IKaptchaService iKaptchaService;

    @Test
    public void getCaptchaTest(){
        KaptchaCodeRequest request = new KaptchaCodeRequest();
        KaptchaCodeResponse response = iKaptchaService.getKaptchaCode(request);
        System.out.println(response.getImageCode());
    }
}
