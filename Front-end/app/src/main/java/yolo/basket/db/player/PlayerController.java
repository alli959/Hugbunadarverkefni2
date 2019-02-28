package yolo.basket.db.player;

import yolo.basket.db.EntityController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: PACKAGE_NAME
 */

public class PlayerController<Ent extends Player> extends EntityController {
	public PlayerController() {
		this.updateURL = "addBooking";
		this.getAllURL = "allBookings";
		this.getOneURL = "oneBooking";
		this.removeURL = "removeBooking";
	}

	public Ent getOnePlayer(long id) throws JSONException {
		List<Ent> listOfOne = getOne(id);
		return listOfOne.get(0);
	}

	public Player jsonToEntity(JSONObject json) throws JSONException {
	    return new Player();
	}
}
