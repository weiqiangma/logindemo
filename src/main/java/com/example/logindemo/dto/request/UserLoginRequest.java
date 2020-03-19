package com.example.logindemo.dto.request;

import com.example.logindemo.dto.request.AbstractRequest;
import com.example.logindemo.exception.ValidateException;
import com.example.logindemo.util.SysRetCodeConstants;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class UserLoginRequest extends AbstractRequest {

    private String userName;

    private String password;

    @Override
    public void requestCheck() {
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            throw new ValidateException(
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }
}
