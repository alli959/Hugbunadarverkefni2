
package search;

import search.generator.Factory;
import search.generator.availability.Availability;
import search.generator.availability.AvailabilityFactory;
import search.generator.review.Review;
import search.generator.review.ReviewFactory;
import search.generator.booking.BookingFactory;
import search.generator.hotel.Hotel;
import search.generator.hotel.HotelEntity;
import search.generator.hotel.HotelFactory;
import search.generator.room.Room;
import search.generator.room.RoomEntity;
import search.generator.room.RoomFactory;
import search.generator.user.User;
import search.generator.user.UserEntity;
import search.generator.user.UserFactory;
import search.generator.ToolBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class API {

	public final HotelFactory<HotelEntity> hotel = new HotelFactory<>();
	public final UserFactory<UserEntity> user = new UserFactory<>();
	public final RoomFactory<RoomEntity> room = new RoomFactory<>();
	public final BookingFactory<Booking> booking = new BookingFactory<>();
	public final AvailabilityFactory<Availability> availability = new AvailabilityFactory<>();
	public final ReviewFactory<Review> review = new ReviewFactory<>();

	private static List<Hotel> allHotels = new ArrayList<>();
	private static boolean hotelListNeedsToBeUpdated = false;

	public static void setHotelListNeedsToBeUpdated(boolean b) {
		hotelListNeedsToBeUpdated = b;
	}

	public List getAllHotels() throws IOException {
		if(!hotelListNeedsToBeUpdated) return allHotels;
		allHotels = hotel.getAll();
		hotelListNeedsToBeUpdated = false;
		return allHotels;
	}

	public HotelEntity getOneHotel(Long id) throws IOException {
		List<HotelEntity> listWithOnlyOneItem = hotel.getOne(id);
		return listWithOnlyOneItem.get(0);
	}

	public Long makeBooking(
		Long   hotelId,
		Long roomId,
		Long userId,
		String dateFrom,
		String dateTo,
		Boolean isPaid,
		Boolean isCancelled,
		String cc
	) throws IOException {
		long from = ToolBox.formatStringDateToLong(dateFrom);
		long to   = ToolBox.formatStringDateToLong(dateTo);
		Booking b = new Booking(hotelId, roomId, userId, from, to, isPaid, isCancelled, cc);
		Long id = booking.save(b);
		return id;
	}

	public Room generateRoomWithAvailability() throws IOException {
		//availability is in DB but not room
		Availability a = new Availability(Factory.getRandomAvailability());
		Long aId = availability.save(a);
		Room r = new Room(
			Factory.getRandom( Factory.roomType),
			1 + Factory.randomInt(3),
			Factory.randomBoolean(),
			aId
		);
		return r;
	}

	public void giveHotelRandomRooms(Hotel hotel) throws IOException {
		//eg er bara ad gefa hoteli max 3 herbergistypur
		//vegna thess ad generatorinn er grimmt lengi ad thessu
		int n = Factory.randomInt(3) + 1;
		for(int i = 0; i < n; i++)
			hotel.addRoomId(room.save(generateRoomWithAvailability()));
		System.out.print("Gave " + n + " rooms to hotel ");
	}

	public void generateHotels(int numberOfHotels) throws IOException {
		int numberOfHotelsEachCountry = numberOfHotels / 3;
		List<Hotel> hotels = new ArrayList<>();
		for(int i = 0; i < numberOfHotelsEachCountry; i++) {
			hotels.add(hotel.generateIcelandic());
			System.out.println("Generating Icelandic hotel... " + i);
		}
		for(int i = 0; i < numberOfHotelsEachCountry; i++) {
			hotels.add(hotel.generateUK());
			System.out.println("Generating UK hotel... " + i);
		}
		for(int i = 0; i < numberOfHotelsEachCountry; i++) {
			hotels.add(hotel.generateFrench());
			System.out.println("Generating French hotel... " + i);
		}
		int i = 0;
		for(Hotel h : hotels) {
			giveHotelRandomRooms(h);
			System.out.println(i++);
			hotel.save(h);
		}
	}

	public void giveUserRandomBookings(User user) throws IOException {
		int n = Factory.randomInt(8) + 1;
		for(int i = 0; i < n; i++) {
			user.addBookingId(booking.save(booking.generate()));
			System.out.println("Creating Booking... " + i);
		}
	}

	public void generateUsers(int numberOfUsers) throws IOException {
		for(int i = 0; i < numberOfUsers; i++) {
			User u = user.generate();
			giveUserRandomBookings(u);
			System.out.println("Creating User... " + i);
			user.save(u);
		}
	}

	public void generateReviews(int numberOfReviews) throws IOException {
		for(int i = 0; i < numberOfReviews; i++) {
			System.out.println("Generating review " + i);
			Review r = review.generate();
			review.save(r);
		}
	}

	public static void main(String [] args) throws IOException {
		API api = new API();
		api.generateReviews(1500);
		api.generateHotels(1500);
		api.generateUsers(500);


		//daemi hvernig madur notar
		API api2 = new API();
		HotelEntity h = api.hotel.generate();
		Long hotelId = api.hotel.save(h);
		System.out.println(api.hotel.getOne(hotelId));
		List<HotelEntity> list = api.hotel.getOne(hotelId);
		h = list.get(0);
		Hotel h2 = (Hotel) h;
		Availability a = api.availability.generate();
		// ... so on
		//
	}
}
