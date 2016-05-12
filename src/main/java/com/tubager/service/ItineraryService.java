package com.tubager.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubager.dao.ItineraryDao;
import com.tubager.domain.TItinerary;
import com.tubager.domain.TSpot;
import com.tubager.utility.Utility;

@Service
public class ItineraryService {
	@Autowired
	private ItineraryDao itineraryDao;
	
	private final static Logger logger = LoggerFactory.getLogger(ItineraryService.class);
	
	public TSpot create(TSpot spot){
		spot.setUuid(Utility.getUuid());
		spot.setDateCreated(new Date());
		String text = spot.getText();
		if(text == null){
			text = "";
		}
		try {
			spot.setContent(text.getBytes("UTF-8"));
			spot.setText("");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TSpot obj = this.itineraryDao.createSpot(spot);
		return obj;
	}
	
	public TItinerary createItinerary(TItinerary itinerary){
		itinerary.setUuid(Utility.getUuid());
		Date today = new Date();
		itinerary.setDateCreated(today);
		itinerary.setLastUpdated(today);
		TItinerary obj = this.itineraryDao.createItinerary(itinerary);
		return obj;
	}
	
	public void removeItinerary(String uuid){
		this.itineraryDao.read(uuid);
	}
	
	public TItinerary updateItinerary(TItinerary itinerary){
		itinerary.setLastUpdated(new Date());
		return this.itineraryDao.updateItinerary(itinerary);
	}
	
	public TItinerary read(String uuid){
		TItinerary obj = this.itineraryDao.read(uuid);
		return obj;
	}
}
