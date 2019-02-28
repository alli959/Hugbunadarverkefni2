package search.generator.availability;

import search.generator.Entity;
import search.generator.Factory;
import search.generator.hotel.Hotel;
import search.generator.hotel.HotelEntity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: PACKAGE_NAME
 */

public class AvailabilityFactory<Ent extends Entity> extends Factory {
	public AvailabilityFactory() {
		this.updateURL = "addAvailability";
		this.getAllURL = "allAvailabilities";
		this.getOneURL = "oneAvailability";
		this.removeURL = "removeAvailability";
	}

	public Availability generate() {
		Availability availability = new Availability(getRandomAvailability());
		return availability;
	}

	@Deprecated
	public Availability jsonToEntity(JSONObject json) {
		return new Availability();
	}
}
