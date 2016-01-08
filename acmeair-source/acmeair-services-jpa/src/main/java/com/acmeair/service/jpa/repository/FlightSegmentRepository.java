package com.acmeair.service.jpa.repository;

import com.acmeair.entities.FlightSegment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liudawei on 15/9/16.
 */

public interface FlightSegmentRepository extends JpaRepository<FlightSegment,String>{

    public FlightSegment findByOriginPortAndDestPort(String originPort, String destPort);
}
