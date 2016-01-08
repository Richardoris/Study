package com.acmeair.service.jpa.repository;

import com.acmeair.entities.AirportCodeMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liudawei on 15/9/16.
 */
public interface AirportCodeMappingRepository extends JpaRepository<AirportCodeMapping,String>{
}
