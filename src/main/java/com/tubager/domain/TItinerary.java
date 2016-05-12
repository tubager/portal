package com.tubager.domain;

import java.util.Date;
import java.util.List;

public class TItinerary {
	private String uuid;
	private String name;
	private String description;
	private String createdBy;
	private Date dateCreated;
	private Date lastUpdated;
	private List<TSpot> spots;

	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public List<TSpot> getSpots() {
		return spots;
	}
	public void setSpots(List<TSpot> spots) {
		this.spots = spots;
	}
}
