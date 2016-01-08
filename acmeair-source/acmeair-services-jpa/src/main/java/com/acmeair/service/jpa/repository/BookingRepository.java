package com.acmeair.service.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.acmeair.entities.Booking;
import com.acmeair.entities.BookingPK;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, BookingPK> {

	public final static String FIND_BY_CUSTOMERID_QUERY = "SELECT b FROM Booking b where b.pkey.customerId = :customerId";

	@Query(FIND_BY_CUSTOMERID_QUERY)
	public List<Booking> findByCustomerId(@Param("customerId") String customerId);
}