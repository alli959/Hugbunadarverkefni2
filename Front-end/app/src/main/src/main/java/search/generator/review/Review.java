/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: server
 */

package search.generator.review;


import search.generator.Entity;
import search.generator.Factory;
import search.generator.ToolBox;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Review extends Entity {

	private Long id;
	private Long userId;
	private Long hotelId;
	private String subject;
	private String reviewText;
	private Double rating;

	public Review() {}

	public Review(Long id, Long userId, Long hotelId, String subject, Map<Integer, Long> bookings) {
		this.id = id;
		this.userId = userId;
		this.hotelId = hotelId;
		this.subject = subject;
	}

	public Review extractEntity() {
		Review review = new Review();
		review.setId(id);
		review.setUserId(userId);
		review.setHotelId(hotelId);
		review.setSubject(subject);
		review.setReviewText(reviewText);
		review.setRating(rating);
		return review;
	}

	@Override
	public List<Pair<String, String>> getParameters() {
		List<Pair<String, String>> params = new ArrayList<>();
		if(this.id != null)
			params.add(new Pair<>("id", "" + id));
		params.add(pair("userId", "" + userId));
		params.add(pair("hotelId", "" + hotelId));
		params.add(pair("subject", "" + subject));
		params.add(pair("rating", "" + rating));
		List<String> textFragments = Factory.splitStringToArrayList(reviewText);
		for(String fragment : textFragments)
			params.add(pair("reviewText", fragment));
		return params;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
}
