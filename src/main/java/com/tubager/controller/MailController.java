package com.tubager.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.service.MailService;

@RestController
public class MailController {
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value="/resetrequest", method=RequestMethod.GET)
	public void resetPassword(@RequestParam("email") String email, HttpServletResponse response){
		mailService.restPassword(email);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
