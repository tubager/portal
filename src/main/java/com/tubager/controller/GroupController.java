package com.tubager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {
	private static Map<String, Integer> map = new HashMap<String, Integer>();
	private static int idx = 0;

	@RequestMapping(value="/group/clear", method=RequestMethod.GET )
	public void clearMap(){
		map.clear();
	}
	

	@RequestMapping(value="/group/get", method=RequestMethod.GET )
	public Integer getGroup(@RequestParam("name")String name){
		if(map.get(name) == null){
			map.put(name, (idx%5)+1);
			idx++;
		}
		return map.get(name);
	}

	@RequestMapping(value="/group/list", method=RequestMethod.GET )
	public Map<String, Integer> list(){
		return map;
	}
}
