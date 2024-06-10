package com.haoren.springbootshirovuebeta.service;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.dao.ArticleDao;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    public JSONObject listArticle(JSONObject jsonObject){

        CommonUtil.fillPagesParams(jsonObject);
        int count = articleDao.countArticle(jsonObject);
        List<JSONObject> articleList = articleDao.listArticle(jsonObject);
        return CommonUtil.successPage(jsonObject,articleList,count);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addArticle(JSONObject article) {
        articleDao.addArticle(article);
        return CommonUtil.successJson();
    }

    public JSONObject updateArticle(JSONObject article) {
        articleDao.updateArticle(article);
        return CommonUtil.successJson();
    }
}
