package com.tubager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.dao.ArticleDao;
import com.tubager.domain.Article;
import com.tubager.domain.TUser;
import com.tubager.service.ArticleService;
import com.tubager.service.UserService;
import com.tubager.utility.Constants;

@RestController
public class ArticleController {
	private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/upload/article", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody String saveArticle(@RequestBody Article article){
		TUser user = userService.getCurrentUser();
		if(user != null){
			article.setUserName(user.getName());
		}
		return articleService.saveArticle(article);
	}

	@RequestMapping(value="/upload/publisharticle", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody String publishArticle(@RequestBody Article article){
		TUser user = userService.getCurrentUser();
		if(user != null){
			article.setUserName(user.getName());
		}
		article.setStatus(Constants.STATUS_PUBLISHED);
		return articleService.saveArticle(article);
	}
	
	@RequestMapping(value="/account/article/{uuid}", method=RequestMethod.GET)
	public @ResponseBody Article readMyArticle(@PathVariable String uuid){
		TUser user = userService.getCurrentUser();
		if(user == null){
			logger.info("user in null");
			return null;
		}
		Article article = articleService.readDraftOrActive(uuid);
		logger.info(article.toString());
		if(article != null && article.getUserName().equalsIgnoreCase(user.getName())){
			return article;
		}
		logger.info(article.getUserName());
		logger.info(user.getName());
		return null;
	}

	@RequestMapping(value="/account/myarticle", method=RequestMethod.GET)
	public @ResponseBody List<Article> myArticles(){
		TUser user = userService.getCurrentUser();
		if(user == null){
			logger.info("user in null");
			return null;
		}
		return articleService.myArticles(user);
	}

	@RequestMapping(value="/toparticles", method=RequestMethod.GET)
	public @ResponseBody List<Article> listTop(){
		return articleService.listTop();
	}
}
