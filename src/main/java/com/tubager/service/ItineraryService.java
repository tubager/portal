package com.tubager.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubager.dao.ItineraryDao;
import com.tubager.domain.Item;
import com.tubager.domain.TItinerary;
import com.tubager.domain.TSpot;
import com.tubager.utility.Constants;
import com.tubager.utility.Utility;

@Service
public class ItineraryService {
	@Autowired
	private ItineraryDao itineraryDao;
	
	private final static Logger logger = LoggerFactory.getLogger(ItineraryService.class);
	
	public String deleteSpot(String uuid, String createdBy){
		TSpot spot = this.itineraryDao.readSpot(uuid);
		if(spot == null){
			return null;
		}
		if(spot.getCreatedBy() == null){
			return null;
		}
		if(!spot.getCreatedBy().equalsIgnoreCase(createdBy)){
			return null;
		}
		return this.itineraryDao.deleteSpot(uuid);
	}
	
	public TSpot readSpot(String uuid){
		TSpot spot = this.itineraryDao.readSpot(uuid);
		List<Item> items = spot.getItems();
		items = this.decodeItems(items);
		spot.setItems(items);
		return spot;
	}
	
	public List<TSpot> listMySpot(String createdBy){
		return this.itineraryDao.listMySpot(createdBy);
	}
	
	public TSpot createSpot(TSpot spot){
		String uuid = Utility.getUuid();
		spot.setUuid(uuid);
		Date date = new Date();
		spot.setDateCreated(date);
		List<Item> items = spot.getItems();
		for(Item item : items){
			item.setBookUuid(uuid);
		}
		items = this.encodeItems(items);
		spot.setItems(items);
		
		TSpot obj = this.itineraryDao.createSpot(spot);
		return obj;
	}
	
	public TSpot saveSpot(TSpot spot){
		List<Item> items = spot.getItems();
		String uuid = spot.getUuid();
		for(Item item : items){
			item.setBookUuid(uuid);
		}
		items = this.encodeItems(items);
		spot.setItems(items);
		this.itineraryDao.deleteSpot(uuid);
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
	
	private List<Item> decodeItems(List<Item> items){
		if(items == null){
			return null;
		}
		for(Item item : items){
			if( Constants.TYPE_TEXT.equalsIgnoreCase(item.getType()) ||
				Constants.TYPE_TIP.equalsIgnoreCase(item.getType()) ||
				Constants.TYPE_SHOP.equalsIgnoreCase(item.getType()) ||
				Constants.TYPE_FOOD.equalsIgnoreCase(item.getType())
			){
				byte[] content = item.getContent();
				if(content == null){
					content = new byte[]{};
				}
				item.setText(new String(content, java.nio.charset.StandardCharsets.UTF_8));
				item.setContent(null);
			}
		}
		return items;
	}
	
	private List<Item> encodeItems(List<Item> items){
		Date date = new Date();
		if(items != null){
			for(Item item : items){
				if(item.getDate() == null){
					item.setDate(date);
				}
				item.setStatus("P");
				if( Constants.TYPE_TEXT.equalsIgnoreCase(item.getType()) ||
					Constants.TYPE_TIP.equalsIgnoreCase(item.getType()) ||
					Constants.TYPE_SHOP.equalsIgnoreCase(item.getType()) ||
					Constants.TYPE_FOOD.equalsIgnoreCase(item.getType())
				){
					String text = item.getText();
					if(text == null){
						text = "";
					}
					try {
						item.setContent(text.getBytes("UTF-8"));
						item.setText("");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return items;
	}
}
