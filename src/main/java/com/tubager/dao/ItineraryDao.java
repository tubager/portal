package com.tubager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubager.domain.Article;
import com.tubager.domain.Item;
import com.tubager.domain.TBook;
import com.tubager.domain.TItinerary;
import com.tubager.domain.TSpot;
import com.tubager.utility.Utility;

@Repository
public class ItineraryDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory.getLogger(ArticleDao.class);
	
	public TSpot createSpot(TSpot spot){
		String insertSql="insert into spot values(?,?,?,?,?,?)";
		jdbcTemplate.update(insertSql, new Object[]{
				spot.getUuid(),
				spot.getName(),
				spot.getDays(),
				spot.getCost(),
				spot.getCreatedBy(),
				Utility.getSqlDate(spot.getDateCreated())});
		List<Item> paraList = spot.getItems();
		insertSql="insert into item values(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Item p = paraList.get(i);
				logger.info(p.getText());
				ps.setString(1, p.getUuid());
				ps.setString(2, p.getBookUuid());
				ps.setString(3, p.getStatus());
				ps.setInt(4, p.getIndex());
				ps.setString(5, p.getType());
				ps.setString(6, p.getText());
				ps.setBytes(7, p.getContent());
				ps.setString(8, p.getSrc());
				ps.setLong(9, p.getLocationId());
				ps.setDate(10, Utility.getSqlDate(p.getDate()));
			}
					
			@Override
			public int getBatchSize() {
				return paraList.size();
			}
		  });
		return spot;
	}
	
	public String deleteSpot(String spotUuid){
		String sql = "delete from spot where uuid = ?";
		jdbcTemplate.update(sql, new Object[]{spotUuid});
		sql = "delete from item where book_uuid = ?";
		jdbcTemplate.update(sql, new Object[]{spotUuid});
		return spotUuid;
	}
	
	public TItinerary createItinerary(TItinerary itinerary){
		String insertSql="insert into itinerary values(?,?,?,?,?,?)";
		jdbcTemplate.update(insertSql, new Object[]{
				itinerary.getUuid(),
				itinerary.getName(),
				itinerary.getDescription(),
				itinerary.getCreatedBy(),
				Utility.getSqlDate(itinerary.getDateCreated()),
				Utility.getSqlDate(itinerary.getLastUpdated())});
//		insertSql="insert into itinerary_spot values(?,?,?)";
		List<TSpot> spots = itinerary.getSpots();
		this.insertSpots(itinerary.getUuid(), spots);
//		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
//			@Override
//			public void setValues(PreparedStatement ps, int i) throws SQLException {
//				TSpot p = spots.get(i);
//				ps.setString(1, itinerary.getUuid());
//				ps.setString(2, p.getUuid());
//				ps.setInt(3, i+1);
//			}
//					
//			@Override
//			public int getBatchSize() {
//				return spots.size();
//			}
//		  });
		return itinerary;
	}
	
	private void insertSpots(String uuid, List<TSpot> spots){
		String insertSql="insert into itinerary_spot values(?,?,?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TSpot p = spots.get(i);
				ps.setString(1, uuid);
				ps.setString(2, p.getUuid());
				ps.setInt(3, i+1);
			}
					
			@Override
			public int getBatchSize() {
				return spots.size();
			}
		  });
	}
	
	public TItinerary updateItinerary(TItinerary itinerary){
		String updateSql = "update itinerary set name=?, description=?, last_updated=? where uuid=?";
		jdbcTemplate.update(updateSql, new Object[]{
				itinerary.getName(),
				itinerary.getDescription(),
				Utility.getSqlDate(itinerary.getLastUpdated()),
				itinerary.getUuid()
			});
		String sql = "delete from itinerary_spot where itinerary_uuid = ?";
		jdbcTemplate.update(sql, new Object[]{itinerary.getUuid()});
		
		List<TSpot> spots = itinerary.getSpots();
		this.insertSpots(itinerary.getUuid(), spots);
		
		return itinerary;
	}
	
	public void removeItinerary(String itineraryUuid){
		logger.info("removing itinerary " + itineraryUuid);
		
		String sql = "delete from itinerary where uuid = ?";
		jdbcTemplate.update(sql, new Object[]{itineraryUuid});
		
		sql = "delete from itinerary_spot where itinerary_uuid = ?";
		jdbcTemplate.update(sql, new Object[]{itineraryUuid});

		logger.info("itinerary deleted for " + itineraryUuid);
	}
	
	public List<TSpot> listMySpot(String createdBy){
		List<TSpot> books = jdbcTemplate.query("select * from spot where created_by=? order by date_created desc", new Object[] {createdBy},
				(rs, rowNum) ->{
					TSpot b = new TSpot();
					b.setUuid(rs.getString("uuid"));
					b.setName(rs.getString("name"));
					b.setDays(rs.getDouble("days"));
					b.setCost(rs.getDouble("cost"));
					b.setCreatedBy(createdBy);
					b.setDateCreated(rs.getDate("date_created"));
					return b;
				});
		
		return books;
	}
	
	public List<TItinerary> listMyItinerary(String createdBy){
		List<TItinerary> books = jdbcTemplate.query("select * from itinerary where created_by=? order by date_created desc", new Object[] {createdBy},
				(rs, rowNum) ->{
					TItinerary b = new TItinerary();
					b.setUuid(rs.getString("uuid"));
					b.setName(rs.getString("name"));
					b.setDescription(rs.getString("description"));
					b.setCreatedBy(createdBy);
					b.setDateCreated(rs.getDate("date_created"));
					return b;
				});
		
		return books;
	}
	
	public TSpot readSpot(String uuid){
		TSpot spot = null;
		List<TSpot> list = jdbcTemplate.query("select * from spot where uuid = ?", new Object[] {uuid},
				(rs, rowNum) ->{
					TSpot b = new TSpot();
					b.setUuid(rs.getString("uuid"));
					b.setName(rs.getString("name"));
					b.setDays(rs.getDouble("days"));
					b.setCost(rs.getDouble("cost"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setDateCreated(rs.getDate("date_created"));
					return b;
				});
		if(list != null && list.size() > 0){
			spot = list.get(0);
		}
		if(spot == null){
			return null;
		}
		List<Item> items = jdbcTemplate.query("select * from item where book_uuid=? order by `index` asc", new Object[] {uuid},
				(rs, rowNum) ->{
					Item graph = new Item();
					graph.setUuid(rs.getString("uuid"));
					graph.setBookUuid(uuid);
					graph.setStatus(rs.getString("status"));
					graph.setIndex(rs.getInt("index"));
					graph.setType(rs.getString("type"));
					graph.setText(rs.getString("text"));
					graph.setContent(rs.getBytes("content"));
					graph.setSrc(rs.getString("src"));
					graph.setLocationId(rs.getLong("location_id"));
					graph.setDate(rs.getDate("date"));
					return graph;
				});
		spot.setItems(items);
		return spot;
	}
	
	public TItinerary read(String uuid){
		TItinerary itinerary = null;
		List<TItinerary> list = jdbcTemplate.query("select * from itinerary where uuid = ?", new Object[] {uuid},
				(rs, rowNum) ->{
					TItinerary b = new TItinerary();
					b.setUuid(rs.getString("uuid"));
					b.setName(rs.getString("name"));
					b.setDescription(rs.getString("description"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setDateCreated(rs.getDate("date_created"));
					return b;
				});
		if(list != null && list.size() > 0){
			itinerary = list.get(0);
		}
		if(itinerary == null){
			return null;
		}
		String query = " select a.* from spot a, itinerary_spot b where a.uuid=b.spot_uuid and b.itinerary_uuid=? order by `idx` asc";
		List<TSpot> spots = jdbcTemplate.query(query, new Object[] {uuid},
				(rs, rowNum) ->{
					TSpot b = new TSpot();
					b.setUuid(rs.getString("uuid"));
					b.setName(rs.getString("name"));
					b.setDays(rs.getInt("days"));
					b.setCost(rs.getDouble("cost"));
					b.setContent(rs.getBytes("content"));
					b.setShopping(rs.getString("shopping"));
					b.setFood(rs.getString("food"));
					b.setCreatedBy(rs.getString("created_by"));
					b.setDateCreated(rs.getDate("date_created"));
					
					byte[] content = b.getContent();
					if(content == null){
						content = new byte[]{};
					}
					b.setText(new String(content, java.nio.charset.StandardCharsets.UTF_8));
					b.setContent(null);
					return b;
				});
		itinerary.setSpots(spots);
		return itinerary;
	}
}
