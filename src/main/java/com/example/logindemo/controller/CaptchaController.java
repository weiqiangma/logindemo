package com.example.logindemo.controller;

import com.example.logindemo.dto.common.ResponseData;
import com.example.logindemo.dto.common.ResponseUtil;
import com.example.logindemo.dto.request.KaptchaCodeRequest;
import com.example.logindemo.dto.response.KaptchaCodeResponse;
import com.example.logindemo.service.IKaptchaService;
import com.example.logindemo.util.CommonResponse;
import com.example.logindemo.util.CookieUtil;
import com.example.logindemo.util.SysRetCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class CaptchaController {

    @Autowired
    IKaptchaService iKaptchaService;

    @Autowired
    CommonResponse commonResponse;

    @RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public ResponseData getKaptchaCode(HttpServletResponse response){
        KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest();
        KaptchaCodeResponse kaptchaCodeResponse = iKaptchaService.getKaptchaCode(kaptchaCodeRequest);
        if(kaptchaCodeResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            Cookie cookie = CookieUtil.genCookie("kaptcha_uuid",kaptchaCodeResponse.getUuid(),"/",60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return new ResponseUtil<>().setData(kaptchaCodeResponse.getImageCode());
        }
        return new ResponseUtil<>().setErrorMsg(kaptchaCodeResponse.getCode());
    }

    @RequestMapping(value = "/kaptcha",method = RequestMethod.POST)
    public ResponseData validKaptchaCode(@RequestBody String code, HttpServletRequest httpServletRequest){
        KaptchaCodeRequest request = new KaptchaCodeRequest();
        String uuid = CookieUtil.getCookieValue(httpServletRequest, "kaptcha_uuid");
        request.setUuid(uuid);
        request.setCode(code);
        KaptchaCodeResponse response = iKaptchaService.validateKaptchaCode(request);
        if(response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            return new ResponseUtil<>().setData(SysRetCodeConstants.SUCCESS.getCode());
        }
        return new ResponseUtil<>().setErrorMsg(response.getCode());
    }
}
