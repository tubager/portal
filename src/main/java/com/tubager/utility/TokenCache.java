package com.tubager.utility;

import org.springframework.scheduling.annotation.Scheduled;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class TokenCache {
	private static Ehcache restApiAuthTokenCache = CacheManager.getInstance().getEhcache("tokenCache");
	private static final int TIME_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
	private static TokenCache instance = new TokenCache();
	
	private TokenCache(){
		CacheManager.getInstance().addCache("tokenCache");
		restApiAuthTokenCache = CacheManager.getInstance().getEhcache("tokenCache");
	}
	
	public static TokenCache getInstance(){
		return instance;
	}
	
	 @Scheduled(fixedRate = TIME_IN_MILLISECONDS)
    public void evictExpiredTokens() {
       restApiAuthTokenCache.evictExpiredElements();
    }
	 
	public void set(String token, String userName){
		Element element = new Element(token, userName);
		restApiAuthTokenCache.put(element);
	}
	
	public String get(String token){
		Element element = restApiAuthTokenCache.get(token);
		 if(element != null){
			 return (String) element.getObjectValue();
		 }
		 return null;
	}
	
	public void remove(String token){
		restApiAuthTokenCache.remove(token);
	}
}
