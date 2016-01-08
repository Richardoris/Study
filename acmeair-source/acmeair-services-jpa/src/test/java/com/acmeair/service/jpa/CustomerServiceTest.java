package com.acmeair.service.jpa;


import static org.junit.Assert.*;

import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.entities.CustomerSession;
import com.acmeair.service.CustomerService;
import com.acmeair.service.jpa.repository.CustomerRepository;
import com.acmeair.service.jpa.repository.CustomerSessionRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liudawei on 15/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class,
//        DbUnitTestExecutionListener.class})
public class CustomerServiceTest {

    @Autowired
    private CustomerService cs;
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private CustomerSessionRepository sessionRepository;
    private Customer c = new Customer();


    @Before
    public void setUp() {
        c.setUsername("aaa");
        c.setPhoneNumber("18510086654");
        c.setAddress(new CustomerAddress("china", "beijing", "beijing", "unknow", "changping", "unknow"));
        c.setMiles_ytd(1000);
        c.setPassword("test");
        c.setPhoneNumberType(Customer.PhoneType.BUSINESS);
        c.setPhoneNumber("1383838438");
        c.setStatus(Customer.MemberShipStatus.EXEC_PLATINUM);
        c.setTotal_miles(200);
        repository.save(c);

    }

    @After
    public void deleteAll() {
        sessionRepository.deleteAll();
        repository.delete(c);
    }

    @Test
    public void getCustomerByUsername() {
        Customer customer = cs.getCustomerByUsername("aaa");
        assertEquals(customer, c);
    }


    @Test
    public void createCustomer() {
        Customer customer = cs.createCustomer("aaa-001", "test", Customer.MemberShipStatus.EXEC_PLATINUM, 200, 1000, "18510086654", Customer.PhoneType.BUSINESS, new CustomerAddress("china", "beijing", "beijing", "unknow", "changping", "unknow"));
        Customer one = repository.findOne("aaa-001");
        assertEquals(customer,one);
    }

    @Test
    public void updateCustomer(){
        final String newpass= "update";
        c.setPassword(newpass);
        cs.updateCustomer(c);
        assertEquals(repository.findOne("aaa").getPassword(), newpass);
    }

    @Test
    public void validateCustomer(){
        assertTrue(cs.validateCustomer("aaa", "test"));
        assertFalse(cs.validateCustomer("aaa", "test1"));
    }

    @Test
    public void getCustomerByUsernameAndPassword(){
        assertNotNull(cs.getCustomerByUsernameAndPassword("aaa", "test"));
        assertNull(cs.getCustomerByUsernameAndPassword("aaa", "bbb"));
    }

    @Test
    public void validateSession(){
        Customer customer = cs.createCustomer("aaa-001", "test", Customer.MemberShipStatus.EXEC_PLATINUM, 200, 1000, "18510086654", Customer.PhoneType.BUSINESS, new CustomerAddress("china", "beijing", "beijing", "unknow", "changping", "unknow"));
        CustomerSession aaa = new CustomerSession("aaa", customer.getUsername(), new Date(),new Date());
        sessionRepository.save(aaa);
        assertNull(cs.validateSession("aaa-1"));
        assertTrue(aaa.getId().equals(cs.validateSession("aaa").getId()));
    }

    @Test
    public void createSession(){
        repository.findOne(c.getUsername());
        CustomerSession session = cs.createSession(c.getUsername());
        CustomerSession one = sessionRepository.findOne(session.getId());
        assertEquals(session, one);
    }

    @Test
    public void invalidateSession(){
        sessionRepository.save(new CustomerSession("testinva",c.getUsername(),new Date(),new Date()));
        cs.invalidateSession("testinva");
        assertNull(sessionRepository.findOne("testinva"));
    }
}
