package com.example;

import java.util.List;

import javax.ejb.Local;

import model.Booking;
import model.Customer;
import model.Restaurant;

@Local
public interface RestaurantBeanLocal {
	public List<Restaurant> getRestaurantsFromDB(String resName);
	
	public List<AvailableRestaurantTimeslot> getAvailableRestaurantTimeslots(String resName);
	
	public boolean makeBooking(Customer customer, Booking booking);
	
}
