package com.haoren.springbootshirovuebeta.filter;


import com.haoren.springbootshirovuebeta.config.annotation.RequiresPermissions;
import com.haoren.springbootshirovuebeta.config.exception.UnauthorizedException;
import com.haoren.springbootshirovuebeta.dto.SessionUserInfo;
import com.haoren.springbootshirovuebeta.service.TokenService;
import com.haoren.springbootshirovuebeta.util.constants.Logical;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;


/**
 * 角色权限控制拦截器
 */
@Slf4j
@Aspect
@Component
@Order(3)
public class PermissionAspect {

    @Autowired
    TokenService tokenService;

    @Before("@annotation(com.haoren.springbootshirovuebeta.config.annotation.RequiresPermissions)")
    public void before(JoinPoint joinPoint){
        log.debug("开始校验");
        SessionUserInfo sessionUserInfo = tokenService.getUserInfo();
        Set<String> ownedPermissionList = sessionUserInfo.getPermissionList();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        RequiresPermissions requiredPermissions = methodSignature.getMethod().getAnnotation(RequiresPermissions.class);
        String[] permissions = requiredPermissions.value();
        log.debug("校验权限:{}", Arrays.toString(permissions));
        log.debug("用户已有权限列表{}", ownedPermissionList);
        if (requiredPermissions.logical() == Logical.AND) {
            for (String permission : permissions){
                if (!ownedPermissionList.contains(permission)){
                    log.error("用户缺少权限: {}",permission);
                    throw new UnauthorizedException();
                }
            }
        }else {
            boolean flag = false;
            for (String permission : permissions){
                if (ownedPermissionList.contains(permission)){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                log.error("用户缺少权限: {}(任意有一种即可)",permissions);
                throw new UnauthorizedException();
            }
        }
    }
}
