package com.example;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Booking;
import model.Customer;
import model.Restaurant;
import model.Timeslot;

import com.sun.xml.ws.rx.rm.runtime.sequence.persistent.PersistenceException;

/**
 * Session Bean implementation class RestaurantBean
 */
@Stateful
public class RestaurantBean implements RestaurantBeanLocal {

    /**
     * Default constructor. 
     */
    public RestaurantBean() {
    }
    
    @PersistenceContext(unitName="RestaurantManagement")
    private EntityManager em;
    
    @PostConstruct
    private void init()
    {
    }

    /*
     * This method gets the list of restaurants from the database
     */
    public List<Restaurant> getRestaurantsFromDB(String resName) {
    	TypedQuery<Restaurant> query;
    	if(resName != null && resName.trim() != "")
    	{
    		//searches list based on user input
    		query = em.createQuery(
    	            "SELECT g FROM Restaurant g WHERE g.name=?1 ORDER BY g.id", Restaurant.class);
    	        query.setParameter(1, resName.trim());
    		 
    	}
    	else
    	{
    		//returns all the restaurants
    		query = em.createQuery(
		            "SELECT g FROM Restaurant g ORDER BY g.id", Restaurant.class);
    	}
        
        return query.getResultList();
    }

    /*
     * This method returns the available time slot for the restaurants
     */
	@Override
	public List<AvailableRestaurantTimeslot> getAvailableRestaurantTimeslots(String resName) {
		List<AvailableRestaurantTimeslot> availRestaurantTimeslots = 
			new ArrayList<AvailableRestaurantTimeslot>();
		//gets the list of restaurants
		List<Restaurant> restaurants = getRestaurantsFromDB(resName);
		
		for (Restaurant restaurant : restaurants) {
			
			String nativeQuery = 
				"SELECT t.* from timeslots t, " +
				"(SELECT rt.timeslot_id, rt.quantity " +
				"FROM restaurant_timeslot rt LEFT OUTER JOIN bookings b " +
				"ON b.restaurant_id=rt.restaurant_id " +
				"WHERE rt.restaurant_id=?1 " +
				"GROUP BY rt.timeslot_id " +
				"HAVING count(b.timeslot_id) < rt.quantity) temp " +
				"WHERE temp.timeslot_id = t.id";
			
	
			Query query = em.createNativeQuery(nativeQuery, Timeslot.class);
			//passes restaurant id as parameter
			query.setParameter(1, restaurant.getId());
			
	        availRestaurantTimeslots.add(
	            new AvailableRestaurantTimeslot(restaurant, query.getResultList()));
		}
		
		return availRestaurantTimeslots;
	}

	/*
	 * It persists the customer details into the customer table.
	 * It persists the booking details in the booking table
	 */
	@Override
	public boolean makeBooking(Customer customer, Booking booking) {
	
		try {
			em.persist(customer);
			em.persist(booking);
		} catch (EntityExistsException | IllegalArgumentException | PersistenceException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
    
}
