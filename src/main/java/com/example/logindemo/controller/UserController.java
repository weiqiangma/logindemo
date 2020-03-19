package com.example.logindemo.controller;


import com.example.logindemo.dto.common.ResponseData;
import com.example.logindemo.dto.common.ResponseUtil;
import com.example.logindemo.dto.request.KaptchaCodeRequest;
import com.example.logindemo.dto.request.UserLoginRequest;
import com.example.logindemo.dto.response.KaptchaCodeResponse;
import com.example.logindemo.dto.response.UserLoginResponse;
import com.example.logindemo.service.IKaptchaService;
import com.example.logindemo.service.IUserService;
import com.example.logindemo.util.CommonResponse;
import com.example.logindemo.util.CookieUtil;
import com.example.logindemo.util.SysRetCodeConstants;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mht
 * @since 2019-12-12
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    IKaptchaService iKaptchaService;

    @Autowired
    IUserService iUserService;


    @PostMapping(value = "/login")
    public ResponseData checklogin(@RequestParam("name") String name, @RequestParam("password")  String password, @RequestParam("captchatext") String captchab, Map<String,String> map, HttpServletRequest request){
        map.put("name",name);
        map.put("password",password);
        map.put("captcha",captchab);
        ResponseUtil responseUtil =new ResponseUtil();
        String captcha = map.get("captcha");
        if(!StringUtils.isBlank(captcha)) {
            //判断验证码是否正确
            String uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
            KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest();
            kaptchaCodeRequest.setUuid(uuid);
            kaptchaCodeRequest.setCode(captcha);
            KaptchaCodeResponse kaptchaCodeResponse = iKaptchaService.validateKaptchaCode(kaptchaCodeRequest);
            if (kaptchaCodeResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
                UserLoginRequest userLoginRequest = new UserLoginRequest();
                userLoginRequest.setUserName(map.get("name"));
                userLoginRequest.setPassword(map.get("password"));
                UserLoginResponse response = iUserService.login(userLoginRequest);
                if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
                    return new ResponseUtil<>().setData(response);
                }
            }
        }
        return new ResponseUtil<>().setErrorMsg(SysRetCodeConstants.KAPTCHA_CODE_ERROR.getMessage());
    }

}
