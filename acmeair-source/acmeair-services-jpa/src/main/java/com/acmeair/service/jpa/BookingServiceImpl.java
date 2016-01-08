package com.acmeair.service.jpa;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.acmeair.entities.*;
import com.acmeair.service.jpa.repository.FlightRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;

import com.acmeair.service.BookingService;
import com.acmeair.service.CustomerService;
import com.acmeair.service.FlightService;
import com.acmeair.service.KeyGenerator;
import com.acmeair.service.jpa.repository.BookingRepository;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	@Resource
	KeyGenerator keyGenerator;

	@Resource
	private FlightService flightService;

	@Resource
	private CustomerService customerService;
	
	@Resource
	private BookingRepository bookingRepository;

	private static String SELECT_ALL_BOOKINGS_BY_USER_ID = "SELECT * FROM booking where customer_id = ?";

	@Override
	@Transactional
	public BookingPK bookFlight(String customerId, FlightPK flightId) {
		String bookingId = keyGenerator.generate().toString();
		Date dateOfBooking = new Date();
		BookingPK key = new BookingPK(customerId, bookingId);

		Booking booking = new Booking(bookingId, dateOfBooking, customerId,
				flightId);

		bookingRepository.save(booking);
		//em.persist(booking);
		return key;
	}

	@Override
	@Transactional
	public Booking getBooking(String user, String id) {
		Customer customer = customerService.getCustomerByUsername(user);

		Booking booking = bookingRepository.findOne(new BookingPK(user, id));

		if(booking != null) {
			Flight flight = flightService.getFlightByFlightKey(new FlightPK(booking.getFlightSegmentId(), booking.getFlightId()));
			booking.setCustomer(customer);
			booking.setFlight(flight);
		}

		return booking;
	}

	@Override
	@Transactional
	public List<Booking> getBookingsByUser(String user) {
		List<Booking> bookings = bookingRepository.findByCustomerId(user);
		Customer customer = customerService.getCustomerByUsername(user);
		for (Booking booking: bookings) {

			booking.setCustomer(customer);

			Flight flight = flightService.getFlightByFlightKey(new FlightPK(booking.getFlightSegmentId(), booking.getFlightId()));

			booking.setFlight(flight);
		}

		return bookings;
	}

	@Override
	@Transactional
	public void cancelBooking(String user, String id) {
		bookingRepository.delete(new BookingPK(user, id));
	}

}
