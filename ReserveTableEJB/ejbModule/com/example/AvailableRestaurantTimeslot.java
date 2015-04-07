package com.example;

import java.util.List;

import model.Restaurant;
import model.RestaurantTimeslot;
import model.Timeslot;

/*
 * This class acts as a wrapper for restaurant object and the corresponding lists of timeslots object
 */
public class AvailableRestaurantTimeslot {
	
	private Restaurant restaurant;
	private List<Timeslot> timeslots;
	
	public AvailableRestaurantTimeslot(Restaurant restaurant, List<Timeslot> timeslots) {
		this.restaurant = restaurant;
		this.timeslots = timeslots;
	}
	
	/**
	 * @return the restaurant
	 */
	public Restaurant getRestaurant() {
		return restaurant;
	}
	/**
	 * @param restaurant the restaurant to set
	 */
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * @return the timeslots
	 */
	public List<Timeslot> getTimeslots() {
		return timeslots;
	}

	/**
	 * @param timeslots the timeslots to set
	 */
	public void setTimeslots(List<Timeslot> timeslots) {
		this.timeslots = timeslots;
	}

}
