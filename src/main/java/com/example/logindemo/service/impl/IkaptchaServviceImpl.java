package com.example.logindemo.service.impl;

import com.example.logindemo.dto.common.ImageResult;
import com.example.logindemo.dto.request.KaptchaCodeRequest;
import com.example.logindemo.dto.response.KaptchaCodeResponse;
import com.example.logindemo.service.IKaptchaService;
import com.example.logindemo.util.SysRetCodeConstants;
import com.example.logindemo.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class IkaptchaServviceImpl implements IKaptchaService {
    @Autowired
    RedissonClient redissonClient;

    private final String KAPTCHA_UUID="kaptcha_uuid";

    /**
     * 获取验证码并保存至redis
     * @param request
     * @return
     */
    @Override
    public KaptchaCodeResponse getKaptchaCode(KaptchaCodeRequest request) {
        KaptchaCodeResponse response = new KaptchaCodeResponse();
        try {
            //生成验证码，ImageResult包含Base64格式图片和验证码文本
            ImageResult capText = VerifyCodeUtils.VerifyCode(140,43,4);
            String uuid = UUID.randomUUID().toString();
            response.setUuid(uuid);
            RBucket rBucket = redissonClient.getBucket(KAPTCHA_UUID + uuid);
            rBucket.set(capText.getCode());
            rBucket.expire(60, TimeUnit.SECONDS);
            response.setImageCode(capText.getImg());
            response.setUuid(uuid);
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        }catch (Exception e){
            log.info("验证码生成失败：" + e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 校验验证码
     * @param request
     * @return
     */
    @Override
    public KaptchaCodeResponse validateKaptchaCode(KaptchaCodeRequest request) {
        KaptchaCodeResponse response = new KaptchaCodeResponse();
        try {
            request.requestCheck();
            String redisKey = KAPTCHA_UUID + request.getUuid();
            RBucket<String> rBucket = redissonClient.getBucket(redisKey);
            String code = rBucket.get();
            log.info("请求的redisKey={},请求的code={},从redis获得的code={}",redisKey,request.getCode(),code);
            if(StringUtils.isNotBlank(code) && request.getCode().equalsIgnoreCase(code)){
                response.setCode(SysRetCodeConstants.SUCCESS.getCode());
                response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
                return response;
            }
            response.setCode(SysRetCodeConstants.KAPTCHA_CODE_ERROR.getCode());
            response.setMsg(SysRetCodeConstants.KAPTCHA_CODE_ERROR.getMessage());
        }catch (Exception e){
            log.info("验证码校验失败：" + e);
            e.printStackTrace();
        }
        return response;
    }
}
