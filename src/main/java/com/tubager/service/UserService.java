package com.tubager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tubager.dao.UserDao;
import com.tubager.domain.CurrentUser;
import com.tubager.domain.TAuth;
import com.tubager.domain.TUser;

@Service
public class UserService implements UserDetailsService{
    @Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		TUser user = userDao.getUser(username);
		TAuth auth = userDao.getAuth(username);
		if(user == null){
			throw new UsernameNotFoundException("User with login name ${username} was not found.");
		}else{
			return new CurrentUser(user, auth);
		}
	}
	
	public TUser getCurrentUser(){
		CurrentUser current = null;
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(obj  instanceof CurrentUser){
			current = (CurrentUser) obj;
			if(current != null){
				return current.getUser();
			}
		}
		TUser user = new TUser();
		user.setName((String) obj);
		return user;
	}
}
