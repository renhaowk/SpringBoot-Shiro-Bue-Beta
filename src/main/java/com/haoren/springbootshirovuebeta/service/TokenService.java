package com.haoren.springbootshirovuebeta.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.haoren.springbootshirovuebeta.config.exception.CommonJsonException;
import com.haoren.springbootshirovuebeta.dao.LoginDao;
import com.haoren.springbootshirovuebeta.dto.SessionUserInfo;
import com.haoren.springbootshirovuebeta.util.StringTool;
import com.haoren.springbootshirovuebeta.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Service
public class TokenService {

    @Autowired
    Cache<String, SessionUserInfo> cacheMap;

    @Autowired
    LoginDao loginDao;


    /**
     * 用户登录验证通过后(sso/帐密),生成token,记录用户已登录的状态
     */
    public String generateToken(String userName) {
        MDC.put("userName", userName);
        String token = UUID.randomUUID().toString().replace("-", "").substring(0,20);
        setCache(token, userName);
        return token;
    }

    public SessionUserInfo getUserInfo(){
        String token = MDC.get("token");
        return getUserInfoFromCache(token);
    }


    private void setCache(String token, String username){
        SessionUserInfo info = getUserInfoByUserName(username);
        log.info("设置用户信息缓存:token={} , username={}, info={}", token, username, info);
        cacheMap.put(token, info);
    }

    private SessionUserInfo getUserInfoByUserName(String username){
        SessionUserInfo userInfo = loginDao.getUserInfo(username);
        if(userInfo.getRoleIds().contains(1)){
            // 系统管理员，可以查看所有菜单
            userInfo.setMenuList(loginDao.getAllMenu());
            userInfo.setPermissionList(loginDao.getAllPermissionCode());
        }
        return userInfo;
    }

    /**
     * 根据token查询用户信息
     * 如果token无效,会抛未登录的异常
     */
    private SessionUserInfo getUserInfoFromCache(String token) {
        if(StringTool.isNullOrEmpty(token)){
            throw new CommonJsonException(ErrorEnum.E_20011);
        }
        SessionUserInfo userInfo = cacheMap.getIfPresent(token);
        if(userInfo == null){
            log.info("没拿到缓存 token={}", token);
            throw new CommonJsonException(ErrorEnum.E_20011);
        }
        return userInfo;
    }

    public void invalidateToken() {
        String token = MDC.get("token");
        if (StringTool.isNullOrEmpty(token)) {
            cacheMap.invalidate(token);
        }
        log.debug("退出登录,清除缓存:token={}", token);
    }
}
