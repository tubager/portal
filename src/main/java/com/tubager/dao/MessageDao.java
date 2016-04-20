package com.tubager.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubager.domain.TMessage;

@Repository
public class MessageDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void addMessage(TMessage message){
		Date date = new Date();
		java.sql.Timestamp today = new java.sql.Timestamp(date.getTime());
		
		String insertSql="insert into message(`name`,`phone`,`email`,`message`,`status`,`date_created`) values(?,?,?,?,?,?)";
		jdbcTemplate.update(insertSql, new Object[]{message.getName(), message.getPhone(),message.getEmail(), message.getMessage(),"N",today});
	}
	
	public List<TMessage> listNewMessages(){
		List<TMessage> messages = jdbcTemplate.query("select * from message where status=? order by date_created asc", new Object[] {"N"},
				(rs, rowNum) ->{
					TMessage b = new TMessage();
					b.setId(rs.getInt("id"));
					b.setName(rs.getString("name"));
					b.setPhone(rs.getString("phone"));
					b.setEmail(rs.getString("email"));
					b.setMessage(rs.getString("message"));
					b.setStatus(rs.getString("status"));
					b.setDateCreated(rs.getDate("date_created"));
					return b;
				});
		return messages;
	}
	
	public List<TMessage> listAllMessages(){
		List<TMessage> messages = jdbcTemplate.query("select * from message order by date_created desc", new Object[] {},
				(rs, rowNum) ->{
					TMessage b = new TMessage();
					b.setId(rs.getInt("id"));
					b.setName(rs.getString("name"));
					b.setPhone(rs.getString("phone"));
					b.setEmail(rs.getString("email"));
					b.setMessage(rs.getString("message"));
					b.setStatus(rs.getString("status"));
					b.setDateCreated(rs.getDate("date_created"));
					return b;
				});
		return messages;
	}

}
