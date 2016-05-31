package com.tubager.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
