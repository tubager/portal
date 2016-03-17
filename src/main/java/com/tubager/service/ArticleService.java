package com.tubager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubager.dao.ArticleDao;
import com.tubager.domain.Article;
import com.tubager.utility.Constants;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	
	private final static Logger logger = LoggerFactory.getLogger(ArticleService.class);
	
	public String saveArticle(Article article){
//		if(article.getUuid() == null){
//			logger.info("creating new book because uuid is null");
//			return this.createArticle(article);
//		}
		String status = article.getStatus();
		if(status == Constants.STATUS_PUBLISHED){
			articleDao.removeHard(article.getUuid());
		}
		else{
			articleDao.removeHard(article.getUuid(), Constants.STATUS_DRAFT);
		}
		return this.createArticle(article);
	}
	
	public void remove(String uuid){
		articleDao.remove(uuid);
	}
	
	private String createArticle(Article article){
		String uuid = articleDao.create(article);
		return uuid;
	}
	
	public Article readActive(String uuid){
		Article article = articleDao.readActive(uuid);
		return article;
	}
	
	public Article readDraftOrActive(String uuid){
		Article article = articleDao.readDraftOrActive(uuid);
		return article;
	}
}
