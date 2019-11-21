-- Table structure for table `Customers`
CREATE TABLE `Customers` (
  `dlicense` varchar(20) NOT NULL,
  `cellphone` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `address` varchar(20) NOT NULL,
   PRIMARY KEY (`dlicense`)
);

-- Dumping data for table `Customers`

INSERT INTO `Customers` (`dlicense`, `cellphone`, `name`, `address`) VALUES
('123bbb', 1234567, 'anon', '111 abc st');

-- Table structure for table `Rentals`

CREATE TABLE `Rentals` (
  `rid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2,
  `confNo` int(11) NOT NULL,
  `startOdometer` double NOT NULL,
  `beginTimestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `returnTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `endOdometer` double NOT NULL,
  `fullTank` tinyint(1) NOT NULL,
  `finalCost` double NOT NULL,
   PRIMARY KEY (`rid`),
   FOREIGN KEY (`confNo`)
);


-- Dumping data for table `Rentals`


INSERT INTO `Rentals` (`rid`, `confNo`, `startOdometer`, `beginTimestamp`, `returnTimestamp`, `endOdometer`, `fullTank`, `finalCost`) VALUES
(1, 123, 12.4, '2019-11-21 02:37:58', '0000-00-00 00:00:00', 12.4, 1, 50.4);

-- Table structure for table `Reservations`

CREATE TABLE `Reservations` (
  `confNo` int(11) NOT NULL,
  `vid` int(11) NOT NULL,
  `dlicense` varchar(20) NOT NULL,
  `startTimestamp` timestamp NOT NULL  DEFAULT '0000-00-00 00:00:00',
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `cardName` varchar(20) NOT NULL,
  `cardNo` varchar(20) NOT NULL,
  `expDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`confNo`),
  FOREIGN KEY (`dlicense`),
  FOREIGN KEY (`vid`);
);

-- Dumping data for table `Reservations`


INSERT INTO `Reservations` (`confNo`, `vid`, `dlicense`, `startTimestamp`, `endTimestamp`, `cardName`, `cardNo`, `expDate`) VALUES
(123, 1, '123bbb', '2019-11-21 02:34:47', '0000-00-00 00:00:00', 'mastercard', '123', '2019-11-21');


-- Table structure for table `Vehicles`


CREATE TABLE `Vehicles` (
  `vid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;,
  `vlicense` varchar(20) NOT NULL,
  `make` varchar(20) NOT NULL,
  `model` varchar(20) NOT NULL,
  `year` int(11) NOT NULL,
  `color` varchar(20) NOT NULL,
  `odometer` double NOT NULL,
  `status` varchar(20) NOT NULL,
  `vtname` varchar(20) NOT NULL,
  `location` varchar(20) NOT NULL,
  PRIMARY KEY (`vid`),
  FOREIGN KEY (`vtname`);
);

-- Dumping data for table `Vehicles`


INSERT INTO `Vehicles` (`vid`, `vlicense`, `make`, `model`, `year`, `color`, `odometer`, `status`, `vtname`, `location`) VALUES
(1, '123abc', 'Honda', 'Civic', 2010, 'blue', 100.5, "available", 'Electrical Vehicle', 'Vancouver');

-- Table structure for table `VehicleTypes`


CREATE TABLE `VehicleTypes` (
  `vtname` varchar(20) NOT NULL,
  `features` text NOT NULL,
  `hourlyRate` double NOT NULL,
  `kiloRate` double NOT NULL,
  `kiloLimitPerHour` double NOT NULL,
  PRIMARY KEY (`vtname`)
);


-- Dumping data for table `VehicleTypes`

INSERT INTO `VehicleTypes` (`vtname`, `features`, `hourlyRate`, `kiloRate`, `kiloLimitPerHour`) VALUES
('Electrical Vehicle', 'environmentally friendly, 4 people capacity', 50, 5, 70);

COMMIT;
