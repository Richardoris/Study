package com.acmeair.service.jpa.repository;

import com.acmeair.entities.CustomerSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

/**
 * Created by liudawei on 15/9/16.
 */
@Repository("customersessionRepository")
public interface CustomerSessionRepository extends JpaRepository<CustomerSession,String>{
}
