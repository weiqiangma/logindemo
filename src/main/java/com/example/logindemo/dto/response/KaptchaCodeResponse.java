package com.example.logindemo.dto.response;

import lombok.Data;

@Data
public class KaptchaCodeResponse extends AbstractResponse {

    private String imageCode;

    private String uuid;
}
