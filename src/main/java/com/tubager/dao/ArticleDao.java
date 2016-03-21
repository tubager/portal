package com.tubager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubager.domain.Article;
import com.tubager.domain.Item;
import com.tubager.domain.TBook;
import com.tubager.utility.Constants;
import com.tubager.utility.Utility;

@Repository
public class ArticleDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory.getLogger(ArticleDao.class);
	
	public String create(Article article){
		String insertSql="insert into article values(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertSql, new Object[]{article.getUuid(),
													article.getTitle(),
													article.getDescription(),
													article.getCoverImg(),
													article.getUserName(),
													article.getLocationName(),
													article.getStatus(),
													Utility.getSqlDate(article.getDateCreated()),
													Utility.getSqlDate(article.getStartDate()),
													Utility.getSqlDate(article.getFinishDate())});
		
		List<Item> paraList = article.getItems();
		insertSql="insert into item values(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Item p = paraList.get(i);
				ps.setString(1, p.getUuid());
				ps.setString(2, p.getBookUuid());
				ps.setString(3, p.getStatus());
				ps.setInt(4, p.getIndex());
				ps.setString(5, p.getType());
				ps.setString(6, p.getText());
				ps.setBytes(7, p.getContent());
				ps.setString(8, p.getSrc());
				ps.setLong(9, p.getLocationId());
				ps.setDate(10, Utility.getSqlDate(p.getDate()));
			}
					
			@Override
			public int getBatchSize() {
				return paraList.size();
			}
		  });
		logger.info("article " + article.getUuid() +" created");
		
		return article.getUuid();
	}
	
	public Article readActive(String uuid){
		return this.read(uuid, "P");
	}
	
	public Article readDraftOrActive(String uuid){
		Article article = this.read(uuid, "D");
		if(article == null){
			article = this.read(uuid, "P");
		}
		return article;
	}
	
	private Article read(String uuid, String status){
		List<Item> paras = jdbcTemplate.query("select * from item where book_uuid=? and status=? order by `index` asc", new Object[] {uuid, status},
				(rs, rowNum) ->{
					Item graph = new Item();
					graph.setUuid(rs.getString("uuid"));
					graph.setBookUuid(uuid);
					graph.setStatus(rs.getString("status"));
					graph.setIndex(rs.getInt("index"));
					graph.setType(rs.getString("type"));
					graph.setText(rs.getString("text"));
					graph.setContent(rs.getBytes("content"));
					graph.setSrc(rs.getString("src"));
					graph.setLocationId(rs.getLong("location_id"));
					graph.setDate(rs.getDate("date"));
					return graph;
				});
		Article book = null;
		List<Article> books = jdbcTemplate.query("select * from article where uuid=? and status=?", new Object[] {uuid, status},
				(rs, rowNum) ->{
					Article b = new Article();
					b.setUuid(rs.getString("uuid"));
					b.setTitle(rs.getString("title"));
					b.setDescription(rs.getString("description"));
					b.setCoverImg(rs.getString("cover_img"));
					b.setUserName(rs.getString("user_name"));
					b.setLocationName(rs.getString("location_name"));
					b.setStatus(rs.getString("status"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setStartDate(rs.getDate("start_date"));
					b.setFinishDate(rs.getDate("finish_date"));
					return b;
				});
		if(books != null && books.size() > 0){
			book = books.get(0);
		}
		if(book != null){
			book.setItems(paras);
			logger.info("article found for " + uuid);
			return book;
		}
		logger.info("article not found for " + uuid);
		return null;
	}
	
	public void removeHard(String bookUuid){
		logger.info("removing article " + bookUuid);
		String sql = "delete from article where uuid = ?";
		jdbcTemplate.update(sql, new Object[]{bookUuid});
		
		sql = "delete from item where book_uuid = ?";
		jdbcTemplate.update(sql, new Object[]{bookUuid});
		logger.info("article deleted for " + bookUuid);
	}
	
	public void removeHard(String bookUuid, String status){
		logger.info("removing article " + bookUuid);
		String sql = "delete from article where uuid = ? and status=?";
		jdbcTemplate.update(sql, new Object[]{bookUuid, status});
		
		sql = "delete from item where book_uuid = ? and status=?";
		jdbcTemplate.update(sql, new Object[]{bookUuid, status});
		logger.info("article deleted for " + bookUuid);
	}
	
	private void setStatus(String uuid, String status){
		String sql = "update article set status=? where uuid=?";
		jdbcTemplate.update(sql, new Object[]{status,uuid});
		
		sql = "update item set status=? where book_uuid=?";
		jdbcTemplate.update(sql, new Object[]{status,uuid});
	}
	
	public void remove(String bookUuid){
		Article active = this.readActive(bookUuid);
		if(active != null){
			this.removeHard(bookUuid, Constants.STATUS_DRAFT);
		}
		this.setStatus(bookUuid, Constants.STATUS_DLETED);
		
		logger.info("article deleted for " + bookUuid);
	}
	
	public List<Article> listTop(){
		//"select * from article where status='D' order by date_created desc limit 5"
		List<Article> books = jdbcTemplate.query("select * from article where status=? order by date_created desc", new Object[] {Constants.STATUS_PUBLISHED},
				(rs, rowNum) ->{
					Article b = new Article();
					b.setUuid(rs.getString("uuid"));
					b.setTitle(rs.getString("title"));
					b.setDescription(rs.getString("description"));
					b.setCoverImg(rs.getString("cover_img"));
					b.setUserName(rs.getString("user_name"));
					b.setLocationName(rs.getString("location_name"));
					b.setStatus(rs.getString("status"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setStartDate(rs.getDate("start_date"));
					b.setFinishDate(rs.getDate("finish_date"));
					return b;
				});
		
		return books;
	}
	
	public List<Article> listMyArticles(String userName){
		List<Article> list = new ArrayList<Article>();
		List<String> draftList = new ArrayList<String>();
		List<Article> books = jdbcTemplate.query("select * from article where user_name=? order by date_created desc", new Object[] {userName},
				(rs, rowNum) ->{
					Article b = new Article();
					b.setUuid(rs.getString("uuid"));
					b.setTitle(rs.getString("title"));
					b.setDescription(rs.getString("description"));
					b.setCoverImg(rs.getString("cover_img"));
					b.setUserName(rs.getString("user_name"));
					b.setLocationName(rs.getString("location_name"));
					b.setStatus(rs.getString("status"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setStartDate(rs.getDate("start_date"));
					b.setFinishDate(rs.getDate("finish_date"));
					return b;
				});
		for(Article a : books){
			if(a.getStatus() == Constants.STATUS_DRAFT){
				draftList.add(a.getUuid());
			}
		}
		for(Article a : books){
			String uuid = a.getUuid();
			String status = a.getStatus();
			if(status == Constants.STATUS_DRAFT){
				list.add(a);
			}
			else if(status == Constants.STATUS_PUBLISHED && !draftList.contains(uuid)){
				list.add(a);
			}
		}
		return list;
	}
	
	public List<Article> search(String text){
		String param = "%"+text+"%";
		List<Article> books = jdbcTemplate.query("select * from book where title like ? or description like ?", new Object[] {param,param},
				(rs, rowNum) ->{
					Article b = new Article();
					b.setUuid(rs.getString("uuid"));
					b.setTitle(rs.getString("title"));
					b.setDescription(rs.getString("description"));
					b.setCoverImg(rs.getString("cover_img"));
					b.setUserName(rs.getString("user_name"));
					b.setLocationName(rs.getString("location_name"));
					b.setStatus(rs.getString("status"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setStartDate(rs.getDate("start_date"));
					b.setFinishDate(rs.getDate("finish_date"));
					return b;
				});
		
		return books;
	}
}
