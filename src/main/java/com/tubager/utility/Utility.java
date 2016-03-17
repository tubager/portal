package com.tubager.utility;

import java.util.UUID;

public class Utility {
	
	public static String getUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static java.sql.Date getSqlDate(java.util.Date date){
		return new java.sql.Date(date.getTime());
	}
}
