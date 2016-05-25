package com.tubager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.domain.TItinerary;
import com.tubager.service.ItineraryService;

@RestController
public class PublicItineraryController {

	@Autowired
	private ItineraryService itineraryService;
	
	@RequestMapping(value="/itinerary/{uuid}", method=RequestMethod.GET, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody TItinerary readItinerary(@PathVariable String uuid){
		TItinerary itinerary = this.itineraryService.read(uuid);
		return itinerary;
	}
	
	@RequestMapping(value="/itinerary/all", method=RequestMethod.GET)
	public @ResponseBody List<TItinerary> listtinerary(){
		List<TItinerary> itinerary = this.itineraryService.listItinerary();
		return itinerary;
	}
}
