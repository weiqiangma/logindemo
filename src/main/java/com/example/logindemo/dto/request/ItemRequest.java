package com.example.logindemo.dto.request;

import lombok.Data;

@Data
public class ItemRequest extends AbstractRequest {
    private Integer pageNum;

    private Integer pageSize;

    @Override
    public void requestCheck() {

    }
}
