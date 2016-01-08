package com.acmeair.service.jpa;

import com.acmeair.entities.AirportCodeMapping;
import com.acmeair.entities.Flight;
import com.acmeair.entities.FlightPK;
import com.acmeair.entities.FlightSegment;
import com.acmeair.service.FlightService;
import com.acmeair.service.jpa.repository.AirportCodeMappingRepository;
import com.acmeair.service.jpa.repository.FlightRepository;
import com.acmeair.service.jpa.repository.FlightSegmentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by liudawei on 15/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
public class FlightServiceTest {
    @Autowired
    private FlightService f;
    @Autowired
    private FlightRepository repository;
    @Autowired
    private FlightSegmentRepository fsRepository;
    @Autowired
    private AirportCodeMappingRepository acmRepository;

    private Flight flight;
    private FlightSegment fs;
    private Date date = new Date();

    @Before
    public void setUp() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date departureTime = c.getTime();

        fs = new FlightSegment("flightSegmentIdTest", "100", "100", 1);
        fsRepository.save(fs);

        flight = new Flight();
        FlightPK flightPK = new FlightPK();
        flightPK.setFlightSegmentId(fs.getFlightName());
        flightPK.setId("flightid");

        flight.setPkey(flightPK);
        flight.setScheduledDepartureTime(departureTime);
        repository.save(flight);
    }
    @After
    public void cleanUp(){
        repository.deleteAll();
        acmRepository.deleteAll();
        fsRepository.deleteAll();


    }

    @Test
    public void getFlightByFlightKey() {
        FlightPK pkey = flight.getPkey();

        Flight flightByFlightKey = f.getFlightByFlightKey(pkey);
        assertTrue(pkey.getId().equals(flightByFlightKey.getPkey().getId()));
        //test cache
        repository.delete(pkey);
        Flight flightCacheTest = f.getFlightByFlightKey(pkey);
        assertTrue(pkey.getId().equals(flightCacheTest.getPkey().getId()));
    }

    @Test
    public void getFlightByAirportsAndDepartureDate() {
        List<Flight> flightByAirportsAndDepartureDate = f.getFlightByAirportsAndDepartureDate("100", "100", date);
        assertNotNull(flightByAirportsAndDepartureDate);
        assertTrue(flightByAirportsAndDepartureDate.get(0).getPkey().getId().equals(flight.getPkey().getId()));
        //test cache
        repository.delete(flight);
        assertNotNull(f.getFlightByAirportsAndDepartureDate("100", "100", date));

        fsRepository.delete(fs);
        assertNotNull(f.getFlightByAirportsAndDepartureDate("100", "100", date).get(0).getFlightSegment());

    }

    @Test
    public void getFlightByAirports() {
        List<Flight> flightByAirports = f.getFlightByAirports("100", "100");
        flight.setFlightSegment(fs);
        assertTrue(flightByAirports.get(0).getPkey().getId().equals(flight.getPkey().getId()));
        assertNotNull(flightByAirports.get(0).getFlightSegment());
    }

    @Test
    public void storeAirportMapping() {
        AirportCodeMapping airportCodeMapping = new AirportCodeMapping("300", "888");
        f.storeAirportMapping(airportCodeMapping);
        AirportCodeMapping one = acmRepository.findOne("300");
        assertTrue(airportCodeMapping.getAirportCode().equals(one.getAirportCode()));
    }

    @Test
    public void storeFlightSegment() {
        fs.setFlightName("teststorefs");
        f.storeFlightSegment(fs);
        FlightSegment teststorefs = fsRepository.findOne("teststorefs");
        assertNotNull(teststorefs);
        assertEquals(fs,teststorefs);

    }

    @Test
    public void createNewFlight() {
        Flight newFlight = f.createNewFlight(fs.getFlightName(), date, date, new BigDecimal(3838438.38), new BigDecimal(3838438.438), 100, 200, "type");
        assertNotNull(newFlight.getPkey().getId());
        Flight testcreate = repository.findOne(newFlight.getPkey());
        assertNotNull(testcreate);
        assertTrue(testcreate.getPkey().getFlightSegmentId().equals(fs.getFlightName()));
    }
}
