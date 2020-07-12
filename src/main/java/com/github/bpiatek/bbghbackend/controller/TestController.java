package com.github.bpiatek.bbghbackend.controller;

import com.github.bpiatek.bbghbackend.dao.ArticleRepository;
import com.github.bpiatek.bbghbackend.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Błażej Rybarkiewicz on 11/07/2020
 */
@RestController
class TestController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    Article test() {
        Article a = Article.builder().content("one to three").title("Very good!").build();
        articleRepository.save(a);
        return a;
    }

}
