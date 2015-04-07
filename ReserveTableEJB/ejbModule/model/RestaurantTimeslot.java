package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the restaurant_timeslot database table.
 * 
 */
@Entity
@Table(name="restaurant_timeslot")
public class RestaurantTimeslot implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private int quantity;

	//bi-directional many-to-one association to Restaurant
	@ManyToOne
	private Restaurant restaurant;

	//bi-directional many-to-one association to Timeslot
	@ManyToOne
	private Timeslot timeslot;

	public RestaurantTimeslot() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Restaurant getRestaurant() {
		return this.restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Timeslot getTimeslot() {
		return this.timeslot;
	}

	public void setTimeslot(Timeslot timeslot) {
		this.timeslot = timeslot;
	}

}