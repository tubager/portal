package com.tubager.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tubager.domain.ItineraryBooking;
import com.tubager.domain.TUser;
import com.tubager.service.BookingService;
import com.tubager.service.UserService;

@RestController
public class BookingController {
	
	private final static Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/account/itinerarybooking", method=RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
	public @ResponseBody ItineraryBooking saveItineraryBooking(@RequestBody ItineraryBooking booking){
		ItineraryBooking itineraryBooking = null;
		TUser user = userService.getCurrentUser();
		if(user == null){
			return null;
		}
		booking.setCreatedBy(user.getName());
		if(booking.getUuid() == null){
			itineraryBooking = bookingService.createItineraryBooking(booking);
		}
		else{
			itineraryBooking = bookingService.readItineraryBooking(booking.getUuid());
			if(itineraryBooking == null){
				return null;
			}
			if(!user.getName().equalsIgnoreCase(itineraryBooking.getCreatedBy())){
				return null;
			}
			itineraryBooking = bookingService.updateItineraryBooking(booking);
		}
		return itineraryBooking;
	}
	
	@RequestMapping(value="/account/itinerarybooking/{uuid}", method=RequestMethod.DELETE)
	public @ResponseBody String removeItineraryBooking(@PathVariable String uuid){
		return bookingService.removeItineraryBooking(uuid);
	}
	
	@RequestMapping(value="/account/itinerarybooking/{uuid}", method=RequestMethod.GET)
	public @ResponseBody ItineraryBooking readMyItineraryBooking(@PathVariable String uuid){
		ItineraryBooking itineraryBooking = null;
		TUser user = userService.getCurrentUser();
		if(user == null){
			return null;
		}
		itineraryBooking = bookingService.readItineraryBooking(uuid);
		if(itineraryBooking == null){
			return null;
		}
		if(!user.getName().equalsIgnoreCase(itineraryBooking.getCreatedBy())){
			return null;
		}
		return itineraryBooking;
	}

	@RequestMapping(value="/account/itinerarybooking/mylist", method=RequestMethod.GET)
	public @ResponseBody List<ItineraryBooking> listMyItineraryBooking(){
		TUser user = userService.getCurrentUser();
		if(user == null){
			logger.info("user is null");
			return null;
		}
		logger.info(user.getName());
		List<ItineraryBooking> bookings = this.bookingService.listMyBookings(user.getName());
		if(bookings == null){
			bookings = new ArrayList<ItineraryBooking>();
		}
		return bookings;
	}

	@RequestMapping(value="/account/itinerarybooking/byowner", method=RequestMethod.GET)
	public @ResponseBody List<ItineraryBooking> listItineraryBookingByOwner(){
		TUser user = userService.getCurrentUser();
		if(user == null){
			return null;
		}
		return this.bookingService.listBookingsByOwner(user.getName());
	}

	@RequestMapping(value="/account/itinerarybooking/byitinerary/{uuid}", method=RequestMethod.GET)
	public @ResponseBody List<ItineraryBooking> listItineraryBookingByItinerary(@PathVariable String uuid){
		return this.bookingService.listBookingsByItinerary(uuid);
	}
}
