package com.acmeair.service.jpa.repository;

import com.acmeair.entities.Flight;
import com.acmeair.entities.FlightPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("flightRepository")
public interface FlightRepository extends CrudRepository<Flight, FlightPK> {

    @Query("SELECT f FROM Flight f WHERE f.pkey.flightSegmentId = ?1 and f.scheduledDepartureTime = ?2")
    List<Flight> findOneByFlightSegmentIdAndScheduledDepartureTime(String flightSegmentId, Date scheduledDepartureTime);

    @Query("SELECT f FROM Flight f WHERE f.pkey.flightSegmentId = ?1")
    List<Flight> findByFlightSegmentId(String flightSegmentId);
}