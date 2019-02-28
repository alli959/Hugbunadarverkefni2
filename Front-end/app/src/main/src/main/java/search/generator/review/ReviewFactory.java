package search.generator.review;

import search.generator.Entity;
import search.generator.Factory;
import search.generator.hotel.Hotel;
import search.generator.hotel.HotelEntity;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: PACKAGE_NAME
 */

public class ReviewFactory<Ent extends Review> extends Factory {
	public ReviewFactory() {
		this.updateURL = "addReview";
		this.getAllURL = "allReviews";
		this.getOneURL = "oneReview";
		this.removeURL = "removeReview";
	}

	public Ent getOneReview(Long id) {
		List<Ent> list = getOne(id);
		return list.get(0);
	}

	@Override
	public Review generate() {
		Review review = new Review();
		review.setHotelId(new Long(randomInt(1500)));
		review.setUserId(new Long(randomInt(50)));
		review.setSubject(getRandom(subject));
		review.setReviewText(lorem);
		review.setRating(randomDouble(0, 10));
		return review;
	}

	@Override
	public Review jsonToEntity(JSONObject json) {
		Long id = getAsLong("id", json);
		Long hotelId = getAsLong("hotelId", json);
		Long userId = getAsLong("userId", json);
		String subject = (String) json.get("subject");
		Double rating = (Double) json.get("rating");
		JSONObject reviewText = (JSONObject) json.get("reviewText");
		Map<Integer, String> textMap = getStringMapFromJSON(reviewText);
		String text = "";
		for(Map.Entry<Integer, String> e : textMap.entrySet()) {
			text += e.getValue();
		}
		Review r = new Review();
		r.setId(id);
		r.setHotelId(hotelId);
		r.setUserId(userId);
		r.setSubject(subject);
		r.setReviewText(text);
		r.setRating(rating);
		return r;
	}
}
