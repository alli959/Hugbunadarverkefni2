package search.generator.user;

import search.generator.Entity;
import search.generator.Factory;
import search.generator.hotel.HotelEntity;
import org.json.JSONObject;
import org.json.JSONException;

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

public class UserFactory<Ent extends UserEntity> extends Factory {
	public UserFactory() {
		this.updateURL = "addUser";
		this.getAllURL = "allUsers";
		this.getOneURL = "oneUser";
		this.removeURL = "removeUser";
	}

	public Ent getOneUser(Long id) throws IOException {
		List<Ent> listOfOne = super.getOne(id);
		return listOfOne.get(0);
	}

	public User generate() {
		User user = new User(
			 getRandom(humanName),
			 getRandom(email)
		);
		return user;
	}

	//muna ad checka a hvad gerist ef draslid er null
	@Deprecated
	public UserEntity jsonToEntity(JSONObject json) {
		Long id = Long.parseLong(json.get("id").toString());
		String name = (String) json.get("name");
		String email = (String) json.get("email");
		Map<Integer, Long> map = new HashMap<>();
		try {
			JSONObject bookingJSON = (JSONObject) json.get("bookingIds");
			map = getLongMapFromJSON(bookingJSON);
		} catch(JSONException e) {}
		return new UserEntity(id, name, email, map);
	}

}
