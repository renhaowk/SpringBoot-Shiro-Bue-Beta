package com.haoren.springbootshirovuebeta.config.exception;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import com.haoren.springbootshirovuebeta.util.constants.ErrorEnum;

/**
 * @author: heeexy
 * @description: 本系统使用的自定义错误类
 * 比如在校验参数时,如果不符合要求,可以抛出此错误类
 * 拦截器可以统一拦截此错误,将其中json返回给前端
 * @date: 2017/10/24 10:29
 */
public class CommonJsonException extends RuntimeException{
    private final JSONObject errJson;

    public CommonJsonException(ErrorEnum errorEnum) {
        this.errJson = CommonUtil.errorJson(errorEnum);
    }

    public CommonJsonException(JSONObject resultJson) {
        this.errJson = resultJson;
    }

    public JSONObject getResultJson() {
        return errJson;
    }
}
