package com.haoren.springbootshirovuebeta.controller;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.service.ArticleService;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping("listArticle")
    public JSONObject listArticle(@RequestBody  JSONObject jsonObject) {
        return articleService.listArticle(jsonObject);
    }

    @GetMapping("addArticle")
    public JSONObject addArticle(@RequestBody JSONObject article) {
        CommonUtil.hasAllRequiredContent(article,"content");
        return articleService.addArticle(article);
    }
}
