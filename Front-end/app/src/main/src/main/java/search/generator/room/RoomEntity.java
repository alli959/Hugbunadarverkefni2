/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: server
 */

package search.generator.room;


import search.generator.Entity;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class RoomEntity extends Entity {
	private Long id;
	private int numberOfBeds;
	private String roomType;
	private boolean extraBed;
	private Long availabilityId;
	private Long hotelId;

	public RoomEntity(String roomType, Integer numberOfBeds, Boolean extraBed) {
		this.roomType = roomType;
		this.numberOfBeds = numberOfBeds;
		this.extraBed = extraBed;
	}

	public RoomEntity(String roomType, int numberOfBeds, boolean extraBed, long availabilityId) {
		this.roomType = roomType;
		this.numberOfBeds = numberOfBeds;
		this.extraBed = extraBed;
		this.availabilityId = availabilityId;
	}

	//private ut af godri astaedu, thessi er bara fyrir extract entity vegna thess ad madur naer id
	private RoomEntity(Long id, String roomType, int numberOfBeds, boolean extraBed, long availabilityId) {
		this.id = id;
		this.numberOfBeds = numberOfBeds;
		this.roomType = roomType;
		this.extraBed = extraBed;
		this.availabilityId = availabilityId;
	}

	public RoomEntity extractEntity() {
		return new RoomEntity(id, roomType, numberOfBeds, extraBed, availabilityId);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public void setHotelId(Long id) {
		this.hotelId = id;
	}

	@Override
	public List<Pair<String, String>> getParameters() {
		List<Pair<String, String>> params = new ArrayList<>();
		if(this.id != null) {
			params.add(pair("id", "" + this.id));
			assert !((this.id + "").equals("null"));
		}
		params.add(pair("numberOfBeds", this.numberOfBeds));
		params.add(pair("roomType", this.roomType));
		params.add(pair("extraBed", this.extraBed));
		params.add(pair("availabilityId", this.availabilityId));
		return params;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public int getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public boolean isExtraBed() {
		return extraBed;
	}

	public void setExtraBed(boolean extraBed) {
		this.extraBed = extraBed;
	}

	public long getAvailabilityId() {
		return availabilityId;
	}

	public void setAvailabilityId(Long availabilityId) {
		this.availabilityId = availabilityId;
	}
}
