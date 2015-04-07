package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the restaurants database table.
 * 
 */
@Entity
@Table(name="restaurants")
public class Restaurant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String cuisine;

	private String location;

	private String name;

	private int rating;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="restaurant", cascade={CascadeType.ALL})
	private List<Booking> bookings;

	//bi-directional many-to-one association to RestaurantTimeslot
	@OneToMany(mappedBy="restaurant", cascade={CascadeType.ALL})
	private List<RestaurantTimeslot> restaurantTimeslots;

	public Restaurant() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuisine() {
		return this.cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Booking addBooking(Booking booking) {
		getBookings().add(booking);
		booking.setRestaurant(this);

		return booking;
	}

	public Booking removeBooking(Booking booking) {
		getBookings().remove(booking);
		booking.setRestaurant(null);

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
		restaurantTimeslot.setRestaurant(this);

		return restaurantTimeslot;
	}

	public RestaurantTimeslot removeRestaurantTimeslot(RestaurantTimeslot restaurantTimeslot) {
		getRestaurantTimeslots().remove(restaurantTimeslot);
		restaurantTimeslot.setRestaurant(null);

		return restaurantTimeslot;
	}

}