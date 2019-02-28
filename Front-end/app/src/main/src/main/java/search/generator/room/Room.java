/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: server.room
 */

package search.generator.room;


import search.generator.availability.Availability;

public class Room extends RoomEntity {
	private Availability availability;

	public Room(String roomType, int numberOfBeds, boolean extraBed, long availabilityId) {
		super(roomType, numberOfBeds, extraBed, availabilityId);
	}

	public Availability getAvailability() {
		return availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
}
