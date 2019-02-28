/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: server
 */

package search.generator.booking;
import search.generator.Entity;
import search.generator.ToolBox;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Booking extends Entity {
	private Long hotelId;
	private Long userId;
	private Long roomId;
	private String dateFrom;
	private String dateTo;
	private Boolean isPaid;
	private Boolean isCancelled;
	private String cc;

	private Long dateFromLong;
	private Long dateToLong;

	public Booking(
		Long hotelId,
		Long roomId,
		Long userId,
		Long from,
		Long to,
		Boolean isCancelled,
		Boolean isPaid,
		String cc
	) {
		this.hotelId = hotelId;
		this.roomId = roomId;
		this.userId = userId;
		this.dateFrom = ToolBox.formatLongDateToString(from);
		this.dateTo = ToolBox.formatLongDateToString(to);
		this.isPaid = isPaid;
		this.isCancelled = isCancelled;
		this.cc = cc;
	}

	public Booking(
		Long id,
		Long hotelId,
		Long roomId,
		Long userId,
		Long from,
		Long to,
		Boolean isCancelled,
		Boolean isPaid,
		String cc
	) {
		this(hotelId, roomId, userId, from, to, isCancelled, isPaid, cc);
		this.id = id;
	}




	@Override
	public List<Pair<String, String>> getParameters() {
		List<Pair<String, String>> params = new ArrayList<>();
		if(this.id != null)
			params.add(new Pair<>("id", "" + this.id));
		params.add(pair("hotelId", this.hotelId));
		params.add(pair("userId", this.userId));
		params.add(pair("roomId", this.roomId));
		params.add(pair("isPaid", "" + this.isPaid));
		params.add(pair("isCancelled", "" + this.isCancelled));

		params.add(pair("dateFrom", this.dateFrom));
		params.add(pair("dateTo", this.dateTo));
		params.add(pair("cc", this.cc));
		return params;
	}

	public void setCC(String cc) {
		this.cc = cc;
	}

	public String getCC() {
		return cc;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean paid) {
		isPaid = paid;
	}

	public String getFrom() {
		return dateFrom;
	}

	public void setFrom(Date from) {
		assert from.getTime() / 86400000 == 0;
		this.dateFrom = ToolBox.formatLongDateToString(from.getTime());
	}

	public String getTo() {
		return dateTo;
	}

	public void setTo(Date to) {
		assert to.getTime() / 86400000 == 0;
		this.dateFrom = ToolBox.formatLongDateToString(to.getTime());
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
