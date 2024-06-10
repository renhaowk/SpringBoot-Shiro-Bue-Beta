package com.haoren.springbootshirovuebeta.dao;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.dto.SessionUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface LoginDao {

    JSONObject checkUser(@Param("userName") String userName, @Param("password") String password);

    SessionUserInfo getUserInfo(String username);

    Set<String> getAllMenu();

    Set<String> getAllPermissionCode();
}
