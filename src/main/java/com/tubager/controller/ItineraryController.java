package com.tubager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.domain.TItinerary;
import com.tubager.domain.TSpot;
import com.tubager.domain.TUser;
import com.tubager.service.ItineraryService;
import com.tubager.service.UserService;

@RestController
public class ItineraryController {
	private final static Logger logger = LoggerFactory.getLogger(ItineraryController.class);

	@Autowired
	private ItineraryService itineraryService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/expert/spot/create", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody TSpot createSpot(@RequestBody TSpot spot){
		TUser user = userService.getCurrentUser();
		if(user != null){
			spot.setCreatedBy(user.getName());
		}
		return this.itineraryService.create(spot);
	}
	
	@RequestMapping(value="/expert/itinerary/create", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody TItinerary createItinerary(@RequestBody TItinerary itinerary){
		TUser user = userService.getCurrentUser();
		if(user != null){
			itinerary.setCreatedBy(user.getName());
		}
		return this.itineraryService.createItinerary(itinerary);
	}
	
	@RequestMapping(value="/expert/itinerary/update", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody TItinerary updateItinerary(@RequestBody TItinerary itinerary){
		return this.itineraryService.updateItinerary(itinerary);
	}
	
	@RequestMapping(value="/expert/itinerary/{uuid}", method=RequestMethod.GET, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody TItinerary readItinerary(@PathVariable String uuid){
		return this.itineraryService.read(uuid);
	}

	@RequestMapping(value="/expert/itinerary/{uuid}", method=RequestMethod.DELETE, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody String removeItinerary(@PathVariable String uuid){
		TUser user = userService.getCurrentUser();
		if(user == null){
			logger.info("user in null");
			return null;
		}
		TItinerary itinerary = this.itineraryService.read(uuid);
		if(itinerary == null){
			logger.info("itinerary in null");
			return null;
		}
		if(!user.getName().equals(itinerary.getCreatedBy())){
			logger.info("can not delete others' itinerary");
			return null;
		}
		this.itineraryService.removeItinerary(uuid);
		return uuid;
	}
}
