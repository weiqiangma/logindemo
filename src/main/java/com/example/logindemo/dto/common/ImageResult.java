package com.example.logindemo.dto.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImageResult implements Serializable {

    private static final long serialVersionUID = 7505997295595095971L;

    private String img;

    private String code;
}
