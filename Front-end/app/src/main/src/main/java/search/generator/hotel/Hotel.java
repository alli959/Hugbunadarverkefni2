package search.generator.hotel;

import search.generator.Entity;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class Hotel extends HotelEntity {
	
	public Hotel(Integer numRooms, String name, String email, Double longitude, double latitude, String imageUrl, Integer stars, Map<Integer, String> amenities, String description) {
		this.setNumRooms(numRooms);
		this.setName(name);
		this.setEmail(email);
		this.setLongitude(longitude);
		this.setLatitude(latitude);
		this.setAmenities(amenities);
		this.setImageUrl(imageUrl);
		this.setStars(stars);
		this.setDescription(description);
	}
}
