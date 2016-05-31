package com.tubager.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubager.domain.ItineraryBooking;
import com.tubager.utility.Utility;

@Repository
public class ItineraryBookingDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory.getLogger(ItineraryBookingDao.class);
	
	public ItineraryBooking createItineraryBooking(ItineraryBooking booking){
		String insertSql="insert into itinerary_booking values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertSql, new Object[]{booking.getUuid(),
													booking.getItineraryUuid(),
													booking.getOwner(),
													booking.getSpotsUUid(),
													booking.getStatus(),
													booking.getCreatedBy(),
													Utility.getSqlDate(booking.getDateCreated()),
													Utility.getSqlDate(booking.getLastUpdated())});
		return booking;
	}
	
	public ItineraryBooking updateItineraryBooking(ItineraryBooking booking){
		String updateSql = "update itinerary_booking set spots_uuid=?, status=?, last_updated=? where uuid=?";
		jdbcTemplate.update(updateSql, new Object[]{
				booking.getSpotsUUid(),
				booking.getStatus(),
				Utility.getSqlDate(booking.getLastUpdated()),
				booking.getUuid()
			});
		return booking;
	}
	
	public ItineraryBooking readItineraryBooking(String uuid){
		ItineraryBooking itinerary = null;
		List<ItineraryBooking> list = jdbcTemplate.query("select * from itinerary_booking where uuid = ?", new Object[] {uuid},
				(rs, rowNum) ->{
					ItineraryBooking b = new ItineraryBooking();
					b.setUuid(rs.getString("uuid"));
					b.setItineraryUuid(rs.getString("itinerary_uuid"));
					b.setOwner(rs.getString("owner"));
					b.setStatus(rs.getString("status"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setLastUpdated(rs.getDate("last_updated"));
					return b;
				});
		if(list != null && list.size() > 0){
			itinerary = list.get(0);
		}
		if(itinerary == null){
			return null;
		}
		return itinerary;
	}
	
	public String removeItineraryBooking(String uuid){
		String sql = "delete from itinerary_booking where uuid = ?";
		jdbcTemplate.update(sql, new Object[]{uuid});
		return uuid;
	}
}
