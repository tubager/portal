package com.tubager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.domain.TUser;
import com.tubager.service.UserService;


@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/currentuser", method=RequestMethod.GET)
	public @ResponseBody TUser getCurrentUser(){
		TUser user = userService.getCurrentUser();
		return user;
	}
}
