package com.haoren.springbootshirovuebeta.service;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.config.exception.CommonJsonException;
import com.haoren.springbootshirovuebeta.dao.LoginDao;
import com.haoren.springbootshirovuebeta.dto.SessionUserInfo;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import com.haoren.springbootshirovuebeta.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    @Autowired
    LoginDao loginDao;

    @Autowired
    TokenService tokenService;

    public JSONObject authLogin(JSONObject requestJson) {
        String userName = requestJson.getString("username");
        String password = requestJson.getString("password");
        JSONObject user =  loginDao.checkUser(userName,password);
        if (user == null){
            throw new CommonJsonException(ErrorEnum.E_10010);
        }
        String token = tokenService.generateToken(userName);
        JSONObject info = new JSONObject();
        info.put("token",token);
        return CommonUtil.successJson(info);
    }
    public JSONObject getInfo(JSONObject requestJson) {
        SessionUserInfo userInfo = tokenService.getUserInfo();
        log.info("userInfo:{}",userInfo);
        return CommonUtil.successJson(userInfo);
    }

    public JSONObject logOut(JSONObject requestJson) {
        tokenService.invalidateToken();
        return CommonUtil.successJson();
    }
}
