package search.generator.hotel;

import search.generator.Entity;
import search.generator.Factory;
import search.API;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: PACKAGE_NAME
 */

public class HotelFactory<Ent extends HotelEntity> extends Factory {
	public HotelFactory() {
		this.updateURL = "addHotel";
		this.getAllURL = "allHotels";
		this.removeURL = "removeHotel";
		this.getOneURL = "oneHotel";
	}

	@Override
	public Hotel generate() {
		return generateIcelandic();
	}

	public Hotel generate(double latitude, double width, double longitude, double height) {
		return new Hotel(
			 randomInt(72),
			 getRandom(hotelName),
			 getRandom(email),
			 randomDouble(latitude, width),
			 randomDouble(longitude, height),
			 getRandom(imageUrl),
			 randomInt(5),
			 getRandomMap(amenities),
			 lorem
		);
	}

	public Hotel generateIcelandic() {
		return generate(
			-23.5, 10.0,
			 63.0, 3.0 );
	}

	public Hotel generateUK() {
		return generate(
			-10.0, 11.6,
			 50.0, 8.0 );
	}

	public Hotel generateFrench() {
		return generate(
			  -3.0, 12.4,
			 42.6,  8.4);
	}

	public Long save(Ent ent) throws IOException {
		API.setHotelListNeedsToBeUpdated(true);
		return super.save(ent);
	}

	public Ent getOneHotel(Long id) throws IOException {
		List<Ent> listOfOne =  super.getOne(id);
		return listOfOne.get(0);
	}

	public Ent jsonToEntity(JSONObject json) {
		Ent ent = (Ent) new HotelEntity();
		ent.setId(Long.parseLong(json.get("id").toString()));
		ent.setNumRooms((int) json.get("numRooms"));
		ent.setLongitude((Double) json.get("longitude"));
		ent.setLatitude((Double) json.get("latitude"));
		ent.setEmail((String) json.get("email"));
		ent.setName((String) json.get("name"));
		ent.setImageUrl((String) json.get("imageUrl"));
		Map<Integer, String> amenities = new HashMap<>();
		Map<Integer, Long> roomIds = new HashMap<>();
		JSONObject amenitiesJSON = (JSONObject) json.get("amenities");
		JSONObject roomIdsJSON = (JSONObject) json.get("roomIds");
		JSONObject description = (JSONObject) json.get("description");
		Map<Integer, String> textMap = getStringMapFromJSON(description);
		String text = "";
		for(Map.Entry<Integer, String> e : textMap.entrySet()) {
			text += e.getValue();
		}
		amenities = getStringMapFromJSON(amenitiesJSON);
		roomIds = getLongMapFromJSON(roomIdsJSON);
		ent.setAmenities(amenities);
		ent.setRoomIds(roomIds);
		ent.setDescription(text);
		return ent;
	}
}
