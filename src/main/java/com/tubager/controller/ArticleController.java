package com.tubager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.domain.Article;
import com.tubager.service.ArticleService;

@RestController
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping(value="/upload/article", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public String saveArticle(@RequestBody Article article){
		return articleService.saveArticle(article);
	}
}
