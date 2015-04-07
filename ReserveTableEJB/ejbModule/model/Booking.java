package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the bookings database table.
 * 
 */
@Entity
@Table(name="bookings")
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="BOOKING_DATE")
	private Date bookingDate;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="PHONE_NUMBER")
	private Customer customer;

	//bi-directional many-to-one association to Restaurant
	@ManyToOne
	private Restaurant restaurant;

	//bi-directional many-to-one association to Timeslot
	@ManyToOne
	private Timeslot timeslot;

	public Booking() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBookingDate() {
		return this.bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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