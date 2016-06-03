package com.tubager.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubager.domain.ItineraryBooking;
import com.tubager.domain.TSpot;
import com.tubager.utility.Utility;

@Repository
public class ItineraryBookingDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory.getLogger(ItineraryBookingDao.class);
	
	public ItineraryBooking createItineraryBooking(ItineraryBooking booking){
		String insertSql="insert into itinerary_booking values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertSql, new Object[]{booking.getUuid(),
													booking.getItineraryUuid(),
													booking.getOwner(),
													booking.getSpotsUUid(),
													booking.getStatus(),
													booking.getCreatedBy(),
													booking.getContact(),
													booking.getPhone(),
													booking.getEmail(),
													booking.getComments(),
													Utility.getSqlDate(booking.getStartDate()),
													Utility.getSqlDate(booking.getReturnDate()),
													booking.getPersons(),
													Utility.getSqlDate(booking.getDateCreated()),
													Utility.getSqlDate(booking.getLastUpdated())});
		return booking;
	}
	
	public ItineraryBooking updateItineraryBooking(ItineraryBooking booking){
		String updateSql = "update itinerary_booking set spots_uuid=?, status=?, last_updated=?, contact=?, phone=?, email=?, comments=?, start_date=?, return_date=?, persons=? where uuid=?";
		jdbcTemplate.update(updateSql, new Object[]{
				booking.getSpotsUUid(),
				booking.getStatus(),
				Utility.getSqlDate(booking.getLastUpdated()),
				booking.getContact(),
				booking.getPhone(),
				booking.getEmail(),
				booking.getComments(),
				Utility.getSqlDate(booking.getStartDate()),
				Utility.getSqlDate(booking.getReturnDate()),
				booking.getPersons(),
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
					b.setSpotsUUid(rs.getString("spots_uuid"));
					b.setOwner(rs.getString("owner"));
					b.setStatus(rs.getString("status"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setContact(rs.getString("contact"));
					b.setPhone(rs.getString("phone"));
					b.setEmail(rs.getString("email"));
					b.setComments(rs.getString("comments"));
					b.setStartDate(rs.getDate("start_date"));
					b.setReturnDate(rs.getDate("return_date"));
					b.setPersons(rs.getInt("persons"));
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
	
	public List<ItineraryBooking> listMyItineraryBookings(String createdBy){
		String sql = "select a.*, b.name, c.nick_name from itinerary_booking a, itinerary b, user c where a.itinerary_uuid=b.uuid and a.owner=c.name and a.created_by=?";
		List<ItineraryBooking> list = jdbcTemplate.query(sql, new Object[] {createdBy},
				(rs, rowNum) ->{
					ItineraryBooking b = new ItineraryBooking();
					b.setUuid(rs.getString("uuid"));
					b.setItineraryUuid(rs.getString("itinerary_uuid"));
					b.setItineraryName(rs.getString("name"));
					b.setOwner(rs.getString("owner"));
					b.setOwnerName(rs.getString("nick_name"));
					b.setStatus(rs.getString("status"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setContact(rs.getString("contact"));
					b.setPhone(rs.getString("phone"));
					b.setEmail(rs.getString("email"));
					b.setComments(rs.getString("comments"));
					b.setStartDate(rs.getDate("start_date"));
					b.setReturnDate(rs.getDate("return_date"));
					b.setPersons(rs.getInt("persons"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setLastUpdated(rs.getDate("last_updated"));
					return b;
				});
		return list;
	}
	
	public List<ItineraryBooking> listBookingsByItinerary(String uuid){
		String sql = "select a.*, c.nick_name from itinerary_booking a, user c where a.created_by=c.name and a.itinerary_uuid=?";
		List<ItineraryBooking> list = jdbcTemplate.query(sql, new Object[] {uuid},
				(rs, rowNum) ->{
					ItineraryBooking b = new ItineraryBooking();
					b.setUuid(rs.getString("uuid"));
					b.setItineraryUuid(rs.getString("itinerary_uuid"));
					b.setOwner(rs.getString("owner"));
					b.setStatus(rs.getString("status"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setCreatedByName(rs.getString("nick_name"));
					b.setContact(rs.getString("contact"));
					b.setPhone(rs.getString("phone"));
					b.setEmail(rs.getString("email"));
					b.setComments(rs.getString("comments"));
					b.setStartDate(rs.getDate("start_date"));
					b.setReturnDate(rs.getDate("return_date"));
					b.setPersons(rs.getInt("persons"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setLastUpdated(rs.getDate("last_updated"));
					return b;
				});
		return list;
	}
	
	public List<ItineraryBooking> listBookingsByOwner(String owner){
		String sql = "select a.*, b.name, c.nick_name from itinerary_booking a, itinerary b, user c where a.itinerary_uuid=b.uuid and a.created_by=c.name and a.owner=?";
		List<ItineraryBooking> list = jdbcTemplate.query(sql, new Object[] {owner},
				(rs, rowNum) ->{
					ItineraryBooking b = new ItineraryBooking();
					b.setUuid(rs.getString("uuid"));
					b.setItineraryUuid(rs.getString("itinerary_uuid"));
					b.setItineraryName(rs.getString("name"));
					b.setOwner(rs.getString("owner"));
					b.setStatus(rs.getString("status"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setCreatedByName(rs.getString("nick_name"));
					b.setContact(rs.getString("contact"));
					b.setPhone(rs.getString("phone"));
					b.setEmail(rs.getString("email"));
					b.setComments(rs.getString("comments"));
					b.setStartDate(rs.getDate("start_date"));
					b.setReturnDate(rs.getDate("return_date"));
					b.setPersons(rs.getInt("persons"));
					b.setDateCreated(rs.getDate("date_created"));
					b.setLastUpdated(rs.getDate("last_updated"));
					return b;
				});
		return list;
	}
	
	private Map<String, TSpot> listSpotsByItinerary(String uuid){
		String sql = "select a.* from spot a, itinerary_spot b where a.uuid=b.spot_uuid and b.itinerary_uuid=?";
		List<TSpot> list = jdbcTemplate.query(sql, new Object[]{uuid},
				(rs, rowNum)->{
					TSpot spot = new TSpot();
					spot.setUuid(rs.getString("uuid"));
					spot.setName(rs.getString("name"));
					spot.setCost(rs.getDouble("cost"));
					spot.setDays(rs.getInt("days"));
					return spot;
				});
		Map<String, TSpot>  map = new HashMap<String, TSpot>();
		for(TSpot s : list){
			map.put(s.getUuid(), s);
		}
		return map;
	}
}
