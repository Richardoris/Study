package com.acmeair.service.jpa;

import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.entities.CustomerSession;
import com.acmeair.service.CustomerService;
import com.acmeair.service.KeyGenerator;
import com.acmeair.service.jpa.repository.CustomerRepository;
import com.acmeair.service.jpa.repository.CustomerSessionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liudawei on 15/9/16.
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    private static final int DAYS_TO_ALLOW_SESSION = 1;

    private static Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Resource
    KeyGenerator keyGenerator;
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private CustomerSessionRepository sessionRepository;

    @Override
    public Customer createCustomer(String username, String password, Customer.MemberShipStatus status, int total_miles, int miles_ytd, String phoneNumber, Customer.PhoneType phoneNumberType, CustomerAddress address) {
        Customer customer = new Customer(username, password, status, total_miles, miles_ytd, address, phoneNumber, phoneNumberType);
        return customerRepository.save(customer);

    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findOne(username);
    }

    @Override
    public boolean validateCustomer(String username, String password) {
        boolean validatedCustomer = false;
        Customer customerToValidate = customerRepository.findOne(username);
        if (customerToValidate != null) {
            validatedCustomer = password.equals(customerToValidate.getPassword());
        }
        return validatedCustomer;
    }


    @Override
    public Customer getCustomerByUsernameAndPassword(String username, String password) {
        Customer c = customerRepository.findOne(username);
        if (!c.getPassword().equals(password)) {
            return null;
        }
        // Should we also set the password to null?
        return c;
    }

    @Override
    public CustomerSession validateSession(String sessionid) {
        return sessionRepository.findOne(sessionid);
    }

    @Override
    public CustomerSession createSession(String customerId) {
        String sessionId = keyGenerator.generate().toString();
        log.info("Create the session Id " + sessionId + " for customer " + customerId);
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_YEAR, DAYS_TO_ALLOW_SESSION);
        Date expiration = c.getTime();
        CustomerSession cSession = new CustomerSession(sessionId, customerId, now, expiration);

        return sessionRepository.save(cSession);
    }

    @Override
    public void invalidateSession(String sessionid) {
        // Here we don't delete the session which is deleted
        if (sessionRepository.exists(sessionid)) {
            sessionRepository.delete(sessionid);
        } else {
            log.info("Remove the Session " + sessionid + " twice.");
        }
    }
}
