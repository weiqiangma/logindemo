package com.example.logindemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.logindemo.dto.common.ResponseData;
import com.example.logindemo.dto.request.UserLoginRequest;
import com.example.logindemo.dto.response.UserLoginResponse;
import com.example.logindemo.entity.User;
import com.example.logindemo.mapper.UserMapper;
import com.example.logindemo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.logindemo.util.JwtTokenUtil;
import com.example.logindemo.util.SysRetCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mht
 * @since 2019-12-12
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    public UserLoginResponse login(UserLoginRequest request){
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        Map<String, Object> map = new HashMap<>();
        map.put("name",request.getUserName());
        map.put("password",DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        log.info(map.get("password").toString());
        try{
            List<User> list = userMapper.selectByMap(map);
            //根据用户名和密码查询
            if(list == null || list.size() == 0) {
                userLoginResponse.setCode(SysRetCodeConstants.USERORPASSWORD_ERRROR.getCode());
                userLoginResponse.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
                return userLoginResponse;
            }

            //判断用户是否已激活
            if("N".equals(list.get(0).getState())) {
                userLoginResponse.setCode(SysRetCodeConstants.USER_ISVERFIED_ERROR.getCode());
                userLoginResponse.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
                return userLoginResponse;
            }

            //加密密码和数据库中比较
            if(!DigestUtils.md5DigestAsHex(request.getPassword().getBytes()).equals(list.get(0).getPassword())){
                userLoginResponse.setCode(SysRetCodeConstants.USERORPASSWORD_ERRROR.getCode());
                userLoginResponse.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
                return userLoginResponse;
            }

            String token = JwtTokenUtil.builder().msg(list.get(0).getId().toString()).build().creatJwtToken();
            BeanUtils.copyProperties(list.get(0),userLoginResponse);
            userLoginResponse.setToken(token);
            userLoginResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            userLoginResponse.setMsg(SysRetCodeConstants.SUCCESS.getMessage());

        } catch (Exception e) {
            log.info("登录出错：" + e);
            e.printStackTrace();
        }
        return userLoginResponse;
    }
}
