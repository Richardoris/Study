/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.acmeair.web;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.stereotype.Component;

import com.acmeair.entities.*;
import com.acmeair.service.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.core.Context;

@Path("/customer")
@Api(value = "/customer", description = "Operations about customer information", protocols = "http")
@Component
public class CustomerREST extends BaseRestService {

	private CustomerService customerService = getWrappedService(CustomerService.class);

	@Context private HttpServletRequest request;


	private boolean validate(String customerid) {
		return true; // TODO: Tomcat Impl throwing weird exception on call to getAttribute
		//		String loginUser = (String) request.getAttribute(RESTCookieSessionFilter.LOGIN_USER);
		//		return customerid.equals(loginUser);
	}

	@GET @Path("/byid/{custid}") @Produces("application/json") @ApiOperation(value = "Get the customer from customer id",
			notes = "This can only be done by the logged in user.",
			response = Customer.class)
	public Response getCustomer(@ApiParam(value = "Session id from the cookie", required = true) @CookieParam("sessionid") String sessionid,
			@ApiParam(value = "Customer id", required = true) @PathParam("custid") String customerid) {
		try {
			// make sure the user isn't trying to update a customer other than the one currently logged in
			if (!validate(customerid)) {
				return Response.status(Response.Status.FORBIDDEN).build();

			}
			Customer customer = customerService.getCustomerByUsername(customerid);

			return Response.ok(customer).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST @Path("/byid/{custid}") @Produces("application/json")
    @ApiOperation(value = "Update the customer information",
            notes = "This can only be done by the logged in user.",
            response = Customer.class)
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Invalid user information") })
	public Response putCustomer(
            @ApiParam(value = "Session id from the cookie", required = true)
            @CookieParam("sessionid") String sessionid,
            @ApiParam(value = "Customer information which need to be updated", required = true)
            Customer customer) {
		if (!validate(customer.getUsername())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		Customer customerFromDB = customerService.getCustomerByUsernameAndPassword(customer.getUsername(), customer.getPassword());
		if (customerFromDB == null) {
			// either the customer doesn't exist or the password is wrong
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		CustomerAddress addressFromDB = customerFromDB.getAddress();
		addressFromDB.setStreetAddress1(customer.getAddress().getStreetAddress1());
		if (customer.getAddress().getStreetAddress2() != null) {
			addressFromDB.setStreetAddress2(customer.getAddress().getStreetAddress2());
		}
		addressFromDB.setCity(customer.getAddress().getCity());
		addressFromDB.setStateProvince(customer.getAddress().getStateProvince());
		addressFromDB.setCountry(customer.getAddress().getCountry());
		addressFromDB.setPostalCode(customer.getAddress().getPostalCode());
		
		customerFromDB.setPhoneNumber(customer.getPhoneNumber());
		customerFromDB.setPhoneNumberType(customer.getPhoneNumberType());
		
		customerService.updateCustomer(customerFromDB);
		customerFromDB.setPassword(null);
		
		return Response.ok(customerFromDB).build();
	}
}
