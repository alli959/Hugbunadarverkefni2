package search.generator.room;

import search.generator.Entity;
import search.generator.Factory;
import search.generator.hotel.Hotel;
import search.generator.hotel.HotelEntity;
import search.generator.user.User;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.IOException;

/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: PACKAGE_NAME
 */

public class RoomFactory<Ent extends RoomEntity> extends Factory {
	public RoomFactory() {
		this.updateURL = "addRoom";
		this.getAllURL = "allRooms";
		this.getOneURL = "oneRoom";
		this.removeURL = "removeRoom";
	}

	public Room generate() {
		Room room = new Room(
			 getRandom(roomType),
			 randomInt(4),
			 randomBoolean(),
			 0
		);
		return room;
	}

	public Ent getOneRoom(Long id) throws IOException {
		List<Ent> list = getOne(id);
		return list.get(0);
	}

	public RoomEntity jsonToEntity(JSONObject json) {
		String roomType = (String) json.get("roomType");
		Integer numberOfBeds = (Integer) json.get("numberOfBeds");
		Boolean extraBed = (Boolean) json.get("extraBed");
		Long availabilityId = Long.parseLong(json.get("availabilityId").toString());
		return new RoomEntity(roomType, numberOfBeds, extraBed, availabilityId);
	}
}
