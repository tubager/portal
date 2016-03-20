package com.tubager.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(value="/checkuserexistence", method=RequestMethod.GET)
	public @ResponseBody boolean checkExistence(@RequestParam("name") String name){
		return userService.checkExistence(name);
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public @ResponseBody void signup(@RequestBody SignUpData data, HttpServletResponse response){
		userService.signup(data.getName(), data.getPassword(), data.getMobile(), data.getEmail());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}

class SignUpData{
	private String name;
	private String password;
	private String mobile;
	private String email;
	private String code;
	
	public String getName() {
		return name;
	}
	public void setName(String userName) {
		this.name = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
