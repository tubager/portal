package com.tubager.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubager.dao.ArticleDao;
import com.tubager.domain.Article;
import com.tubager.domain.Item;
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
		
		Date date = new Date();
		if(article.getDateCreated() == null){
			article.setDateCreated(date);
		}
		if(article.getStartDate() == null){
			article.setStartDate(date);
		}
		if(article.getFinishDate() == null){
			article.setFinishDate(date);
		}
		
		List<Item> items = article.getItems();
		if(items != null){
			for(Item item : items){
				if(item.getDate() == null){
					item.setDate(date);
				}
				item.setStatus(status);
				if(Constants.TYPE_TEXT.equalsIgnoreCase(item.getType()) || Constants.TYPE_TIP.equalsIgnoreCase(item.getType())){
					String text = item.getText();
					if(text == null){
						text = "";
					}
					try {
						item.setContent(text.getBytes("UTF-8"));
						item.setText("");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return this.createArticle(article);
	}
	
	public void remove(String uuid){
		articleDao.remove(uuid);
	}
	
	//private
	private String createArticle(Article article){
		String uuid = articleDao.create(article);
		return uuid;
	}
	
	public List<Article> listTop(){
		return this.articleDao.listTop();
	}
	
	public Article readActive(String uuid){
		Article article = articleDao.readActive(uuid);
		List<Item> items = article.getItems();
		this.processItem(items);
		return article;
	}
	
	public Article readDraftOrActive(String uuid){
		Article article = articleDao.readDraftOrActive(uuid);
		List<Item> items = article.getItems();
		this.processItem(items);
		return article;
	}
	
	//private
	private void processItem(List<Item> items){
		if(items == null){
			return;
		}
		for(Item item : items){
			if(item.getType() == Constants.TYPE_TEXT || item.getType() == Constants.TYPE_TIP){
				byte[] content = item.getContent();
				if(content == null){
					content = new byte[]{};
				}
				item.setText(new String(content, java.nio.charset.StandardCharsets.UTF_8));
			}
		}
	}
}
