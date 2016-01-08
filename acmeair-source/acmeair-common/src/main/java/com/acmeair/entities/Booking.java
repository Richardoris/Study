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
package com.acmeair.entities;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="booking")
public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bookingPK")
	private BookingPK pkey;

	@Column(name="flight_id")
	private String flightId;

	private String flightSegmentId;

	@Transient
	private Customer customer;

	@Transient
	private Flight flight;

	@Column(name="booking_date")
	private Date dateOfBooking;

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getFlightSegmentId() {
		return flightSegmentId;
	}

	public void setFlightSegmentId(String flightSegmentId) {
		this.flightSegmentId = flightSegmentId;
	}

	public Booking() {
	}

	public Booking(String id, Date dateOfFlight, Customer customer,
			Flight flight) {
		this.pkey = new BookingPK(customer.getUsername(), id);
		this.flight = flight;
		this.customer = customer;
		this.flightId = flight.getPkey().getId();
		this.flightSegmentId = flight.getPkey().getFlightSegmentId();
		this.dateOfBooking = dateOfFlight;
	}

	public Booking(String id, Date dateOfFlight, String customeId,
			FlightPK flightKey) {
		this.pkey = new BookingPK(customeId, id);
		this.flightId = flightKey.getId();
		this.flightSegmentId = flightKey.getFlightSegmentId();
		this.dateOfBooking = dateOfFlight;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public BookingPK getPkey() {
		return pkey;
	}

	public void setPkey(BookingPK pkey) {
		this.pkey = pkey;
	}

	public Date getDateOfBooking() {
		return dateOfBooking;
	}

	public void setDateOfBooking(Date dateOfBooking) {
		this.dateOfBooking = dateOfBooking;
	}

	@Override
	public String toString() {
		return "Booking [key=" + pkey + ", flightKey=[id=" + flightId
				+ ", flightSegmentId=" + flightSegmentId + "], dateOfBooking="
				+ dateOfBooking + ", customerId=" + pkey.getCustomerId()
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;

		return propEquals(dateOfBooking, other.dateOfBooking) &&
		propEquals(flightId, other.flightId)  &&
		propEquals(flightSegmentId, other.flightSegmentId) &&
		propEquals(pkey, other.pkey);

	}

	private boolean propEquals(Object p1, Object p2) {

		return (p1 == null && p2 == null) || (p1 != null && p2 != null && p1.equals(p2));
		
	}

}
