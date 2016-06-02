package com.tubager.domain;

import java.util.Date;
import java.util.List;

public class ItineraryBooking {
	private String uuid;
	private String itineraryUuid;
	private String owner;
	private String ownerName;
	private List<TSpot> spots;
	private String itineraryName;
	private String spotsUUid;
	private String status;
	private String createdBy;
	private String createdByName;
	private String contact;
	private String phone;
	private String email;
	private String comments;
	private Date startDate;
	private Date returnDate;
	private int persons;
	private Date dateCreated;
	private Date lastUpdated;

	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public int getPersons() {
		return persons;
	}
	public void setPersons(int persons) {
		this.persons = persons;
	}
	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public List<TSpot> getSpots() {
		return spots;
	}
	public void setSpots(List<TSpot> spots) {
		this.spots = spots;
	}
	public String getItineraryName() {
		return itineraryName;
	}
	public void setItineraryName(String itineraryName) {
		this.itineraryName = itineraryName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getItineraryUuid() {
		return itineraryUuid;
	}
	public void setItineraryUuid(String itineraryUuid) {
		this.itineraryUuid = itineraryUuid;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSpotsUUid() {
		return spotsUUid;
	}
	public void setSpotsUUid(String spotsUUid) {
		this.spotsUUid = spotsUUid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
