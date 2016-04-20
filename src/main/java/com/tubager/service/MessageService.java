package com.tubager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubager.dao.MessageDao;
import com.tubager.domain.TMessage;

@Service
public class MessageService {
	@Autowired
	private MessageDao messageDao;
	
	public void addMessage(TMessage message){
		this.messageDao.addMessage(message);
	}
	
	public List<TMessage> listNewMessage(){
		return this.messageDao.listNewMessages();
	}
	
	public List<TMessage> listAllMessage(){
		return this.messageDao.listAllMessages();
	}
}
