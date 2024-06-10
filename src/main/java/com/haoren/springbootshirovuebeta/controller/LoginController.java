package com.haoren.springbootshirovuebeta.controller;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.service.LoginService;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("auth")
    public JSONObject login(JSONObject requestJson) {
        CommonUtil.hasAllRequiredContent(requestJson, "username,password");
        return loginService.authLogin(requestJson);
    }

    @PostMapping("getInfo")
    public JSONObject getInfo(JSONObject requestJson){
        return loginService.getInfo(requestJson);
    }

    @PostMapping("logout")
    public JSONObject logout(JSONObject requestJson) {
        return loginService.logOut(requestJson);
    }
}
