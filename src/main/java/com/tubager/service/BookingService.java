package com.tubager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubager.dao.ItineraryBookingDao;
import com.tubager.domain.CurrentUser;
import com.tubager.domain.ItineraryBooking;
import com.tubager.domain.TItinerary;
import com.tubager.domain.TSpot;
import com.tubager.domain.TUser;
import com.tubager.utility.Utility;

@Service
public class BookingService {
	
	@Autowired
	private ItineraryBookingDao itineraryBookingDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ItineraryService itineraryService;
	
	public ItineraryBooking readItineraryBooking(String uuid){
		ItineraryBooking booking = itineraryBookingDao.readItineraryBooking(uuid);
		CurrentUser currentuser = (CurrentUser) userService.loadUserByUsername(booking.getCreatedBy());
		if(currentuser != null){
			TUser user = currentuser.getUser();
			if(user != null){
				booking.setCreatedByName(user.getNickName()); 
			}
		}
		currentuser = (CurrentUser) userService.loadUserByUsername(booking.getOwner());
		if(currentuser != null){
			TUser user = currentuser.getUser();
			if(user != null){
				booking.setOwnerName(user.getNickName()); 
			}
		}
		
		TItinerary itinerary = itineraryService.read(booking.getItineraryUuid());
		if(itinerary != null){
			booking.setItineraryName(itinerary.getName());
			List<TSpot> list = itinerary.getSpots();
			String[] uuids = booking.getSpotsUUid().split(",");
			List<TSpot> spots = new ArrayList<TSpot>();
			for(String sid : uuids){
				for(TSpot spot : list){
					if(sid.equalsIgnoreCase(spot.getUuid())){
						spots.add(spot);
					}
				}
			}
			booking.setSpots(spots);
		}
		
		return booking;
	}
	
	public ItineraryBooking createItineraryBooking(ItineraryBooking booking){
		String uuid = Utility.getUuid();
		booking.setUuid(uuid);
		Date date = new Date();
		booking.setDateCreated(date);
		booking.setLastUpdated(date);
		ItineraryBooking itineraryBooking = itineraryBookingDao.createItineraryBooking(booking);
		return itineraryBooking;
	}
	
	public ItineraryBooking updateItineraryBooking(ItineraryBooking booking){
		ItineraryBooking itineraryBooking = itineraryBookingDao.updateItineraryBooking(booking);
		return itineraryBooking;
	}
	
	public String removeItineraryBooking(String uuid){
		return itineraryBookingDao.removeItineraryBooking(uuid);
	}
}
