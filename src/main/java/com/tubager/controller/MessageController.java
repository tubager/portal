package com.tubager.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.domain.TMessage;
import com.tubager.service.MessageService;

@RestController
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value="/message", method = RequestMethod.POST)
	public @ResponseBody void addMessage(@RequestBody TMessage message, HttpServletResponse response){
		this.messageService.addMessage(message);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(value="/account/admin/newmessages", method = RequestMethod.GET)
	public @ResponseBody List<TMessage> listNewMessages(){
		return this.messageService.listNewMessage();
	}

	@RequestMapping(value="/account/admin/allmessages", method = RequestMethod.GET)
	public @ResponseBody List<TMessage> listAllMessages(){
		return this.messageService.listAllMessage();
	}
}
