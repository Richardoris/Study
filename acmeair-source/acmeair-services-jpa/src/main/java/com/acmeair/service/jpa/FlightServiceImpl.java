package com.acmeair.service.jpa;

import com.acmeair.entities.*;
import com.acmeair.service.CustomerService;
import com.acmeair.service.FlightService;
import com.acmeair.service.KeyGenerator;
import com.acmeair.service.jpa.repository.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liudawei on 15/9/16.
 */
@Service("flightService")
public class FlightServiceImpl implements FlightService {
    private static ConcurrentHashMap<FlightPK, Flight> flightPKtoFlightCache = new ConcurrentHashMap<FlightPK, Flight>();
    private static ConcurrentHashMap<String, FlightSegment> originAndDestPortToSegmentCache = new ConcurrentHashMap<String, FlightSegment>();
    private static ConcurrentHashMap<String, List<Flight>> flightSegmentAndDataToFlightCache = new ConcurrentHashMap<String, List<Flight>>();
    @Resource
    KeyGenerator keyGenerator;
    @Resource
    private FlightRepository flightRepository;
    @Resource
    private FlightSegmentRepository flightSegmentRepository;
    @Resource
    private AirportCodeMappingRepository airportCodeMappingRepository;

    @Override
    public Flight getFlightByFlightKey(FlightPK key) {
        Flight flight = flightPKtoFlightCache.get(key);
        if (flight == null) {
            flight = flightRepository.findOne(key);
            if (key != null && flight != null) {
                flightPKtoFlightCache.putIfAbsent(key, flight);
            }
        }
        return flight;
    }

    @Override
    public List<Flight> getFlightByAirportsAndDepartureDate(String fromAirport, String toAirport, Date deptDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(deptDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date departureTime = c.getTime();

        String originPortAndDestPortQueryString = fromAirport + toAirport;
        FlightSegment segment = originAndDestPortToSegmentCache.get(originPortAndDestPortQueryString);
        if (segment == null) {
            segment = flightSegmentRepository.findByOriginPortAndDestPort(fromAirport, toAirport);
            if (segment == null) {
                segment = new FlightSegment(); // put a sentinel value of a non-populated flightsegment
            }
            originAndDestPortToSegmentCache.putIfAbsent(originPortAndDestPortQueryString, segment);
        }
        // cache flights that not available (checks against sentinel value above indirectly)
        if (segment.getFlightName() == null) {
            return new ArrayList<Flight>();
        }

        String segId = segment.getFlightName();
        String flightSegmentIdAndScheduledDepartureTimeQueryString = segId + deptDate.toString();
        List<Flight> flights = flightSegmentAndDataToFlightCache.get(flightSegmentIdAndScheduledDepartureTimeQueryString);

        if (flights == null) {
            flights = flightRepository.findOneByFlightSegmentIdAndScheduledDepartureTime(segment.getFlightName(), departureTime);
            for (Flight f : flights) {
                f.setFlightSegment(segment);
            }
            flightSegmentAndDataToFlightCache.putIfAbsent(flightSegmentIdAndScheduledDepartureTimeQueryString, flights);
        }
        return flights;
    }

    @Override
    public List<Flight> getFlightByAirports(String fromAirport, String toAirport) {
        FlightSegment segment = flightSegmentRepository.findByOriginPortAndDestPort(fromAirport, toAirport);
        List<Flight> flights = flightRepository.findByFlightSegmentId(segment.getFlightName());
        for (Flight f : flights) {
            f.setFlightSegment(segment);
        }
        return flights;
    }

    @Override
    public void storeAirportMapping(AirportCodeMapping mapping) {
        airportCodeMappingRepository.save(mapping);

    }

    @Override
    public Flight createNewFlight(String flightSegmentId, Date scheduledDepartureTime, Date scheduledArrivalTime, BigDecimal firstClassBaseCost, BigDecimal economyClassBaseCost, int numFirstClassSeats, int numEconomyClassSeats, String airplaneTypeId) {
        String id = keyGenerator.generate().toString();
        Flight flight = new Flight(id, flightSegmentId,
                scheduledDepartureTime, scheduledArrivalTime,
                firstClassBaseCost, economyClassBaseCost,
                numFirstClassSeats, numEconomyClassSeats,
                airplaneTypeId);
        return flightRepository.save(flight);
    }

    @Override
    public void storeFlightSegment(FlightSegment flightSeg) {
        flightSegmentRepository.save(flightSeg);
    }

    @Override
    public void closeDatasource() {

    }
}
