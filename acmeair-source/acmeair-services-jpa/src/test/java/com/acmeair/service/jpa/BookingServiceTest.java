package com.acmeair.service.jpa;

import com.acmeair.entities.*;
import com.acmeair.service.BookingService;
import com.acmeair.service.CustomerService;
import com.acmeair.service.FlightService;
import com.acmeair.service.jpa.repository.BookingRepository;
import com.acmeair.service.jpa.repository.CustomerRepository;
import com.acmeair.service.jpa.repository.FlightRepository;
import com.acmeair.service.jpa.repository.FlightSegmentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestPersistenceConfig.class })

// @DatabaseSetup("booking.xml")
// @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {
// "booking.xml" })
public class BookingServiceTest {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private FlightSegmentRepository flightSegmentRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BookingService bookingService;

	private String defaultUser = "user-1";

	private Customer customer;
	private Flight flight;
	private FlightSegment fs;
	private Date date = new Date();

	@Before
	public void setUp() {

		fs = new FlightSegment("flightSegmentId-1", "100", "100", 1);

		flight = new Flight();
		FlightPK flightPK = new FlightPK();
		flightPK.setFlightSegmentId(fs.getFlightName());
		flightPK.setId("flightId-1");

		flight.setPkey(flightPK);
		flight.setScheduledDepartureTime(date);

		CustomerAddress address = new CustomerAddress("123 Main St.", null, "AnyTown", "NC", "USA", "27617");
		customer = new Customer(defaultUser, "password", Customer.MemberShipStatus.GOLD,
				1000000, 1000, address, "919-123-4567",
				Customer.PhoneType.BUSINESS);

		flightSegmentRepository.save(fs);

		flightRepository.save(flight);

		customerRepository.save(customer);
	}

	@After
	public void deleteAll() {
		bookingRepository.deleteAll();
		flightRepository.deleteAll();
		flightSegmentRepository.deleteAll();
		customerRepository.deleteAll();
	}

	@Test
	public void bookFlight() {

		BookingPK pk = bookingService.bookFlight(defaultUser, this.flight.getPkey());

		Booking newBooking = bookingService.getBooking(pk.getCustomerId(), pk.getId());

		assertNotNull(newBooking);

		assertEquals(this.flight.getPkey(), newBooking.getFlight().getPkey());
		assertEquals(this.customer, newBooking.getCustomer());
		assertEquals(pk, newBooking.getPkey());
	}

	@Test
	public void cancelBooking() {

		BookingPK pk = bookingService.bookFlight(defaultUser, this.flight.getPkey());

		bookingService.cancelBooking(pk.getCustomerId(), pk.getId());

		Booking newBooking = bookingService.getBooking(pk.getCustomerId(), pk.getId());

		assertNull("a object is found by a deleted object's primary key", newBooking);
	}

	@Test
	public void getBookingsByUser() {

		BookingPK pk = bookingService.bookFlight(defaultUser, this.flight.getPkey());

		List<Booking> bookings = bookingService.getBookingsByUser(defaultUser);

		assertEquals(1, bookings.size());

		Booking newBooking = bookings.get(0);

		assertNotNull(newBooking);
		assertEquals(this.flight.getPkey(), newBooking.getFlight().getPkey());
		assertEquals(this.customer, newBooking.getCustomer());

		assertEquals(pk, newBooking.getPkey());
	}

}
