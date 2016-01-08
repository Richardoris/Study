package com.acmeair.service.jpa.repository;

import com.acmeair.entities.Booking;
import com.acmeair.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, String> {

}