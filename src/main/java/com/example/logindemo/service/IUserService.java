package com.example.logindemo.service;

import com.example.logindemo.dto.request.UserLoginRequest;
import com.example.logindemo.dto.response.UserLoginResponse;
import com.example.logindemo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mht
 * @since 2019-12-12
 */
public interface IUserService extends IService<User> {

    UserLoginResponse login(UserLoginRequest request);
}
