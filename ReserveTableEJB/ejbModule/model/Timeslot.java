package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the timeslots database table.
 * 
 */
@Entity
@Table(name="timeslots")
public class Timeslot implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="TIMESLOT_NAME")
	private String timeslotName;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="timeslot", cascade={CascadeType.ALL})
	private List<Booking> bookings;

	//bi-directional many-to-one association to RestaurantTimeslot
	@OneToMany(mappedBy="timeslot", cascade={CascadeType.ALL})
	private List<RestaurantTimeslot> restaurantTimeslots;

	public Timeslot() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimeslotName() {
		return this.timeslotName;
	}

	public void setTimeslotName(String timeslotName) {
		this.timeslotName = timeslotName;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Booking addBooking(Booking booking) {
		getBookings().add(booking);
		booking.setTimeslot(this);

		return booking;
	}

	public Booking removeBooking(Booking booking) {
		getBookings().remove(booking);
		booking.setTimeslot(null);

		return booking;
	}

	public List<RestaurantTimeslot> getRestaurantTimeslots() {
		return this.restaurantTimeslots;
	}

	public void setRestaurantTimeslots(List<RestaurantTimeslot> restaurantTimeslots) {
		this.restaurantTimeslots = restaurantTimeslots;
	}

	public RestaurantTimeslot addRestaurantTimeslot(RestaurantTimeslot restaurantTimeslot) {
		getRestaurantTimeslots().add(restaurantTimeslot);
		restaurantTimeslot.setTimeslot(this);

		return restaurantTimeslot;
	}

	public RestaurantTimeslot removeRestaurantTimeslot(RestaurantTimeslot restaurantTimeslot) {
		getRestaurantTimeslots().remove(restaurantTimeslot);
		restaurantTimeslot.setTimeslot(null);

		return restaurantTimeslot;
	}

}