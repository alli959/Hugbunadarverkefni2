package search.generator.user;

import search.generator.Entity;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserEntity extends Entity {

	private Long id;
	private String name;
	private String email;
	//bookingNumber and bookingId
	private Map<Integer, Long> bookings = new HashMap<>();

	public UserEntity(Long id, String name, String email, Map<Integer, Long> bookings) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.bookings = bookings;
	}

	public UserEntity(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public boolean hasBooking(Booking b) {
		if(b.getId() == null) throw new IllegalArgumentException("Booking doesn't have an ID");
		Map<Integer, Long> bookings = getBookings();
		for(int i = 0; i < bookings.size(); i++) {
			if(b.getId().equals(bookings.get(i)))
				return true;
		}
		return false;
	}

	public UserEntity extractEntity() {
		return new UserEntity(id, name, email, bookings);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public List<Pair<String, String>> getParameters() {
		List<Pair<String, String>> params = new ArrayList<>();
		if(this.id != null)
			params.add(new Pair<>("id", "" + this.id));
		params.add(new Pair<>("name", "" + this.name));
		params.add(new Pair<>("email", "" + this.email));
		if(bookings.size() != 0) {
			params.addAll(mapToListOfPairs("bookingIds", bookings));
		}
		return params;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Map<Integer, Long> getBookings() {
		return bookings;
	}

	public void addBookingId(Long bookingId) {
		int bookingNumber = bookings.size();
		this.bookings.put(bookingNumber, bookingId);
	}
}
