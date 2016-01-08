
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS customersession;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS flight;
DROP TABLE IF EXISTS flightSegment;
DROP TABLE IF EXISTS airportCodeMapping;

CREATE TABLE booking (
  customer_id varchar(100) NOT NULL,
  id varchar(100) NOT NULL,
  flight_id varchar(100) NOT NULL,
  flightSegmentId varchar(100) NOT NULL,
  booking_date timestamp NOT NULL,
  PRIMARY KEY (customer_id,id)
);


CREATE TABLE `customer` (
  `id` varchar(100) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `total_miles` int(11) DEFAULT NULL,
  `miles_ytd` int(11) DEFAULT NULL,
  `phoneNumber` varchar(45) DEFAULT NULL,
  `phoneNumberType` varchar(45) DEFAULT NULL,
  `streetAddress1` varchar(500) DEFAULT NULL,
  `streetAddress2` varchar(500) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `stateProvince` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `postalCode` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `customersession` (
  `id` varchar(100) NOT NULL,
  `customerid` varchar(45) ,
  `lastAccessedTime` timestamp DEFAULT CURRENT_TIMESTAMP,
  `timeoutTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `customerid_id` FOREIGN KEY (`customerid`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


CREATE TABLE `flightSegment`(
  `id` varchar(100) NOT NULL,
  `originPort` varchar(100) DEFAULT NULL,
  `destPort` varchar(100) DEFAULT NULL,
  `miles` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `flight` (
  `id` varchar(100) NOT NULL,
  `flightSegmentId` varchar(100),
  `scheduledDepartureTime` timestamp DEFAULT CURRENT_TIMESTAMP,
  `scheduledArrivalTime` timestamp DEFAULT CURRENT_TIMESTAMP,
  `firstClassBaseCost` decimal(25,0) DEFAULT NULL,
  `economyClassBaseCost` decimal(25,0) DEFAULT NULL,
  `numFirstClassSeats` int(11) DEFAULT NULL,
  `numEconomyClassSeats` int(11) DEFAULT NULL,
  `airplaneTypeId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`, `flightSegmentId`, `scheduledDepartureTime`),
  CONSTRAINT `flight_flight_id` FOREIGN KEY (`flightSegmentId`) REFERENCES `flightSegment` (`id`) on DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `airportCodeMapping` (
  `id` varchar(100) NOT NULL,
  `airportName` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
