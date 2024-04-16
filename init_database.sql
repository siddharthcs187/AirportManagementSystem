CREATE DATABASE IF NOT EXISTS airportdb;
-- drop database airportdb;
USE airportdb; 

CREATE TABLE IF NOT EXISTS airlines (
    IATA_Code VARCHAR(255) PRIMARY KEY,
    Airline_Name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS flights (
    Flight_No INT AUTO_INCREMENT PRIMARY KEY,
    Date_Time DATETIME,
    Is_Arriving BOOLEAN,
    Gate INT,
    Origin VARCHAR(255),
    Destination VARCHAR(255),
    Baggage_Belt INT,
    Checkin_Lane INT,
    IATA_Code VARCHAR(255),
    Status VARCHAR(30),
    FOREIGN KEY (IATA_Code) REFERENCES airlines(IATA_Code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS passengers (
    Pass_ID INT AUTO_INCREMENT PRIMARY KEY,
    First_name VARCHAR(255),
    Last_name VARCHAR(255),
    DOB DATE,
    Flight_No INT,
    Privilege VARCHAR(30),
    Mobile_No VARCHAR(10), 
    Red_Flag BOOLEAN DEFAULT false,
    FOREIGN KEY (Flight_No) REFERENCES flights(Flight_No) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bags (
    Bag_ID INT AUTO_INCREMENT PRIMARY KEY,
    Pass_ID INT,
    Weight FLOAT,
    Is_Secure BOOLEAN DEFAULT true,
    Is_Fragile BOOLEAN DEFAULT false,
    Priority BOOLEAN DEFAULT false,
    Flight_No INT,
    Lost BOOLEAN,
    FOREIGN KEY (Pass_ID) REFERENCES passengers(Pass_ID) ON DELETE CASCADE,
    FOREIGN KEY (Flight_No) REFERENCES flights(Flight_No) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS staff (
    Staff_ID INT AUTO_INCREMENT PRIMARY KEY,
    Role VARCHAR(255),
    IATA_Code VARCHAR(255),
    First_name VARCHAR(255),
    Last_name VARCHAR(255),
    FOREIGN KEY (IATA_Code) REFERENCES airlines(IATA_Code) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS inventory (
    I_ID INT AUTO_INCREMENT PRIMARY KEY,
    Type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ground_assets (
    I_ID INT,
    Flight_No INT,
    PRIMARY KEY (I_ID, Flight_No),
    FOREIGN KEY (I_ID) REFERENCES inventory(I_ID) ON DELETE CASCADE,
    FOREIGN KEY (Flight_No) REFERENCES flights(Flight_No) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS shops (
    Shop_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    Floor_No INT,
    Terminal_No INT,
    Category VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS shopping (
    Order_No INT AUTO_INCREMENT PRIMARY KEY,
    Shop_ID INT,
    Pass_ID INT,
    Amount INT,
    FOREIGN KEY (Shop_ID) REFERENCES shops(Shop_ID) ON DELETE CASCADE,
    FOREIGN KEY (Pass_ID) REFERENCES passengers(Pass_ID) ON DELETE SET NULL
);

-- trigger to ensure any first class passenger added to the table will always have priority baggage
DELIMITER //

CREATE TRIGGER set_bag_priority
BEFORE INSERT ON bags
FOR EACH ROW
BEGIN
  IF (SELECT Privilege FROM passengers WHERE Pass_ID = NEW.Pass_ID) = 'first class' THEN
    SET NEW.Priority = 1;
  END IF;
END//

DELIMITER ;

-- trigger to set baggage belt/ check in lane based on whether it is incoming or outgoing flight
DELIMITER //

CREATE TRIGGER set_checkin_lane_baggage_belt
BEFORE INSERT ON flights
FOR EACH ROW
BEGIN
    IF NEW.Is_Arriving = 1 THEN
        SET NEW.Checkin_Lane = -1;
    ELSE
        SET NEW.Baggage_Belt = -1;
    END IF;
END//

DELIMITER ;

show tables;

-- 5 airlines
insert into airlines values ('6E', 'Indigo');
insert into airlines values ('AI', 'Air India');
insert into airlines values ('SJ', 'SpiceJet');
insert into airlines values ('G8', 'GoAir');
insert into airlines values ('VA', 'Vistara');

-- 10 flights, 2 of each airline
insert into flights values (1, '2024-04-23 10:00:00', 1, 1, 'DEL', 'BOM', 1, 1, '6E', 'Delayed');
insert into flights values (2, '2024-04-23 11:00:00', 0, 2, 'BOM', 'DEL', 2, 2, 'AI', 'Delayed');
insert into flights values (3, '2024-04-23 12:00:00', 1, 3, 'DEL', 'BLR', 3, 3, 'SJ', 'Arrived');
insert into flights values (4, '2024-04-23 13:00:00', 0, 4, 'BLR', 'DEL', 4, 4, 'G8', 'Boarding');
insert into flights values (5, '2024-04-23 14:00:00', 1, 5, 'DEL', 'CCU', 5, 5, 'VA', 'On Time');
insert into flights values (6, '2024-04-23 15:00:00', 0, 6, 'CCU', 'DEL', 6, 6, '6E', 'Delayed');
insert into flights values (7, '2024-04-23 16:00:00', 1, 7, 'DEL', 'HYD', 7, 7, 'AI', 'On Time');
insert into flights values (8, '2024-04-23 17:00:00', 0, 8, 'HYD', 'DEL', 8, 8, 'SJ', 'Boarding');
insert into flights values (9, '2024-04-23 18:00:00', 1, 9, 'DEL', 'MAA', 9, 9, 'G8', 'Arrived');
insert into flights values (10, '2024-04-23 19:00:00', 0, 10, 'MAA', 'DEL', 10, 10, 'VA', 'On Time');

-- 100 passengers, 10 on each flight
-- 2 first class, 2 business and 6 economy on each flight
insert into passengers values (1, 'John', 'Doe', '1990-01-01', 1, "economy", '1234567890', false);
insert into passengers values (2, 'Jane', 'Doe', '1991-01-01', 2, "economy", '1234567891', false);
insert into passengers values (3, 'Alice', 'Smith', '1992-01-01', 3, "economy", '1234567892', false);
insert into passengers values (4, 'Bob', 'Smith', '1993-01-01', 4, "economy", '1234567893', false);
insert into passengers values (5, 'Charlie', 'Brown', '1994-01-01', 5, "economy", '1234567894', false);
insert into passengers values (6, 'David', 'Brown', '1995-01-01', 6, "economy", '1234567895', false);
insert into passengers values (7, 'Eve', 'Johnson', '1996-01-01', 7, "economy", '1234567896', false);
insert into passengers values (8, 'Frank', 'Johnson', '1997-01-01', 8, "economy", '1234567897', false);
insert into passengers values (9, 'Grace', 'Williams', '1998-01-01', 9, "economy", '1234567898', false);
insert into passengers values (10, 'Henry', 'Williams', '1999-01-01', 10, "economy", '1234567899', false);
insert into passengers values (11, 'Ivy', 'Martinez', '2000-01-01', 1, "business", '1234567800', false);
insert into passengers values (12, 'Jack', 'Martinez', '2001-01-01', 2, "business", '1234567810', false);
insert into passengers values (13, 'Kelly', 'Garcia', '2002-01-01', 3, "business", '1234567820', false);
insert into passengers values (14, 'Larry', 'Garcia', '1989-01-01', 4, "business", '1234567830', false);
insert into passengers values (15, 'Mia', 'Lopez', '1988-01-01', 5, "business", '1234567840', false);
insert into passengers values (16, 'Nancy', 'Lopez', '1987-01-01', 6, "business", '1234567850', false);
insert into passengers values (17, 'Oliver', 'Perez', '1986-01-01', 7, "business", '1234567860', false);
insert into passengers values (18, 'Pam', 'Perez', '1985-01-01', 8, "business", '1234567870', false);
insert into passengers values (19, 'Quinn', 'Rodriguez', '1984-01-01', 9, "business", '1234567880', false);
insert into passengers values (20, 'Ryan', 'Rodriguez', '1983-01-01', 10, "business", '1234567000', false);
insert into passengers values (21, 'Sara', 'Hernandez', '1982-01-01', 1, "first class", '1234567100', false);
insert into passengers values (22, 'Tom', 'Hernandez', '1981-01-01', 2, "first class", '1234567200', false);
insert into passengers values (23, 'Uma', 'Gonzalez', '1980-01-01', 3, "first class", '1234567300', false);
insert into passengers values (24, 'Vince', 'Gonzalez', '1990-02-01', 4, "first class", '1234567400', false);
insert into passengers values (25, 'Wendy', 'Torres', '1990-03-01', 5, "first class", '1234567500', false);
insert into passengers values (26, 'Xavier', 'Torres', '1990-04-01', 6, "first class", '1234567600', false);
insert into passengers values (27, 'Yara', 'Ramirez', '1990-05-01', 7, "first class", '1234567700', false);
insert into passengers values (28, 'Zack', 'Ramirez', '1990-06-01', 8, "first class", '1234567800', false);
insert into passengers values (29, 'Ava', 'Sanchez', '1990-07-01', 9, "first class", '2234567890', false);
insert into passengers values (30, 'Ben', 'Sanchez', '1990-08-01', 10, "first class", '3234567890', false);
insert into passengers values (31, 'Cara', 'Brown', '1990-09-01', 1, "economy", '4234567890', false);
insert into passengers values (32, 'Dale', 'Brown', '1990-10-01', 2, "economy", '5234567890', false);
insert into passengers values (33, 'Ella', 'Johnson', '1990-11-01', 3, "economy", '2345678900', false);
insert into passengers values (34, 'Finn', 'Johnson', '1990-12-01', 4, "economy", '7234567890', false);
insert into passengers values (35, 'Gina', 'Williams', '1990-01-02', 5, "economy", '8234567890', false);
insert into passengers values (36, 'Hank', 'Williams', '1990-01-03', 6, "economy", '9234567890', false);
insert into passengers values (37, 'Iris', 'Martinez', '1990-01-04', 7, "economy", '1034567890', false);
insert into passengers values (38, 'Jack', 'Martinez', '1990-01-05', 8, "economy", '1134567890', false);
insert into passengers values (39, 'Kara', 'Garcia', '1990-01-06', 9, "economy", '1121111890', false);
insert into passengers values (40, 'Liam', 'Garcia', '1990-01-07', 10, "economy", '1234167890', false);
insert into passengers values (41, 'Mia', 'Lopez', '1990-01-08', 1, "business", '1234563490', false);
insert into passengers values (42, 'Nate', 'Lopez', '1990-01-09', 2, "business", '1234561890', false);
insert into passengers values (43, 'Olivia', 'Perez', '1990-01-10', 3, "business", '1236567890', false);
insert into passengers values (44, 'Pete', 'Perez', '1990-01-11', 4, "business", '1234667890', false);
insert into passengers values (45, 'Quinn', 'Rodriguez', '1990-01-12', 5, "business", '1111111111', false);
insert into passengers values (46, 'Ryan', 'Rodriguez', '1990-01-13', 6, "business", '1111111112', false);
insert into passengers values (47, 'Sara', 'Hernandez', '1990-01-14', 7, "business", '1111111113', false);
insert into passengers values (48, 'Tom', 'Hernandez', '1990-01-15', 8, "business", '1111111114', false);
insert into passengers values (49, 'Uma', 'Gonzalez', '1990-01-16', 9, "business", '1111111115', false);
insert into passengers values (50, 'Vince', 'Gonzalez', '1990-01-17', 10, "business", '1111111116', false);
insert into passengers values (51, 'Wendy', 'Torres', '1990-01-18', 1, "first class", '1111111117', false);
insert into passengers values (52, 'Xavier', 'Torres', '1990-01-19', 2, "first class", '1111111118', false);
insert into passengers values (53, 'Yara', 'Ramirez', '1990-01-20', 3, "first class", '1111111119', false);
insert into passengers values (54, 'Zack', 'Ramirez', '1990-01-21', 4, "first class", '1111111110', false);
insert into passengers values (55, 'Ava', 'Sanchez', '1990-01-22', 5, "first class", '1111111121', false);
insert into passengers values (56, 'Ben', 'Sanchez', '1990-01-23', 6, "first class", '1111111131', false);
insert into passengers values (57, 'Cara', 'Brown', '1990-01-24', 7, "first class", '1111111141', false);
insert into passengers values (58, 'Dale', 'Brown', '1990-01-25', 8, "first class", '1111111151', false);
insert into passengers values (59, 'Ella', 'Johnson', '1990-01-26', 9, "first class", '1111111161', false);
insert into passengers values (60, 'Finn', 'Johnson', '1990-01-27', 10, "first class", '1111111171', false);
insert into passengers values (61, 'Gina', 'Williams', '1990-01-28', 1, "economy", '1111111181', false);
insert into passengers values (62, 'Hank', 'Williams', '1990-01-29', 2, "economy", '1111111191', false);
insert into passengers values (63, 'Iris', 'Martinez', '1990-01-30', 3, "economy", '2111111111', false);
insert into passengers values (64, 'Jack', 'Martinez', '1990-01-31', 4, "economy", '3111111111', false);
insert into passengers values (65, 'Kara', 'Garcia', '1890-01-01', 5, "economy", '4111111111', false);
insert into passengers values (66, 'Liam', 'Garcia', '1970-01-01', 6, "economy", '5111111111', false);
insert into passengers values (67, 'Mia', 'Lopez', '1960-01-01', 7, "economy", '6111111111', false);
insert into passengers values (68, 'Nate', 'Lopez', '1950-01-01', 8, "economy", '7111111111', false);
insert into passengers values (69, 'Olivia', 'Perez', '1940-01-01', 9, "economy", '8111111111', false);
insert into passengers values (70, 'Pete', 'Perez', '1930-01-01', 10, "economy", '9111111111', false);
insert into passengers values (71, 'Quinn', 'Rodriguez', '1920-01-01', 1, "economy", '0111111111', false);
insert into passengers values (72, 'Ryan', 'Rodriguez', '1910-01-01', 2, "economy", '2222222222', false);
insert into passengers values (73, 'Sara', 'Hernandez', '1900-01-01', 3, "economy", '2222222221', false);
insert into passengers values (74, 'Tom', 'Hernandez', '1971-01-01', 4, "economy", '2222222220', false);
insert into passengers values (75, 'Uma', 'Gonzalez', '1972-01-01', 5, "economy", '2222222223', false);
insert into passengers values (76, 'Vince', 'Gonzalez', '1973-01-01', 6, "economy", '2222222224', false);
insert into passengers values (77, 'Wendy', 'Torres', '1974-01-01', 7, "economy", '2222222225', false);
insert into passengers values (78, 'Xavier', 'Torres', '1975-01-01', 8, "economy", '2222222226', false);
insert into passengers values (79, 'Yara', 'Ramirez', '1965-01-01', 9, "economy", '2222222227', false);
insert into passengers values (80, 'Zack', 'Ramirez', '1964-01-01', 10, "economy", '2222222228', false);
insert into passengers values (81, 'Ava', 'Sanchez', '1963-01-01', 1, "economy", '2222222229', false);
insert into passengers values (82, 'Ben', 'Sanchez', '1962-01-01', 2, "economy", '2222222211', false);
insert into passengers values (83, 'Cara', 'Brown', '1961-01-01', 3, "economy", '3333333333', false);
insert into passengers values (84, 'Dale', 'Brown', '1966-01-01', 4, "economy", '3333333335', false);
insert into passengers values (85, 'Ella', 'Johnson', '1967-01-01', 5, "economy", '3333333330', false);
insert into passengers values (86, 'Finn', 'Johnson', '1968-01-01', 6, "economy", '3333333331', false);
insert into passengers values (87, 'Gina', 'Williams', '1969-01-01', 7, "economy", '3333333332', false);
insert into passengers values (88, 'Hank', 'Williams', '1976-01-01', 8, "economy", '3333333334', false);
insert into passengers values (89, 'Iris', 'Martinez', '1977-01-01', 9, "economy", '3333333336', false);
insert into passengers values (90, 'Jack', 'Martinez', '1978-01-01', 10, "economy", '3333333337', false);
insert into passengers values (91, 'Kara', 'Garcia', '1970-01-01', 1, "economy", '3333333338', false);
insert into passengers values (92, 'Liam', 'Garcia', '1991-10-05', 2, "economy", '3333333339', false);
insert into passengers values (93, 'Mia', 'Lopez', '1991-09-01', 3, "economy", '3333333300', false);
insert into passengers values (94, 'Nate', 'Lopez', '1991-08-01', 4, "economy", '4444444444', false);
insert into passengers values (95, 'Olivia', 'Perez', '1991-07-01', 5, "economy", '4444444440', false);
insert into passengers values (96, 'Pete', 'Perez', '1991-06-01', 6, "economy", '4444444441', false);
insert into passengers values (97, 'Quinn', 'Rodriguez', '1991-05-01', 7, "economy", '4444444442', false);
insert into passengers values (98, 'Ryan', 'Rodriguez', '1991-04-01', 8, "economy", '4444444443', false);
insert into passengers values (99, 'Sara', 'Hernandez', '1991-03-01', 9, "economy", '4444444445', false);
insert into passengers values (100, 'Tom', 'Hernandez', '1991-02-01', 10, "economy", '4444444446', false);

-- 50 staff, 10 in each airline, 5 roles, 2 of same role per airline
insert into staff values (1, 'Pilot', '6E', 'John', 'Doe');
insert into staff values (2, 'Cabin Crew', '6E', 'Jane', 'Doe');
insert into staff values (3, 'Ground Staff', '6E', 'Alice', 'Smith');
insert into staff values (4, 'Security', '6E', 'Bob', 'Smith');
insert into staff values (5, 'Catering', '6E', 'Charlie', 'Brown');
insert into staff values (6, 'Pilot', 'AI', 'David', 'Brown');
insert into staff values (7, 'Cabin Crew', 'AI', 'Eve', 'Johnson');
insert into staff values (8, 'Ground Staff', 'AI', 'Frank', 'Johnson');
insert into staff values (9, 'Security', 'AI', 'Grace', 'Williams');
insert into staff values (10, 'Catering', 'AI', 'Henry', 'Williams');
insert into staff values (11, 'Pilot', 'SJ', 'Ivy', 'Martinez');
insert into staff values (12, 'Cabin Crew', 'SJ', 'Jack', 'Martinez');
insert into staff values (13, 'Ground Staff', 'SJ', 'Kelly', 'Garcia');
insert into staff values (14, 'Security', 'SJ', 'Larry', 'Garcia');
insert into staff values (15, 'Catering', 'SJ', 'Mia', 'Lopez');
insert into staff values (16, 'Pilot', 'G8', 'Nancy', 'Lopez');
insert into staff values (17, 'Cabin Crew', 'G8', 'Oliver', 'Perez');
insert into staff values (18, 'Ground Staff', 'G8', 'Pam', 'Perez');
insert into staff values (19, 'Security', 'G8', 'Quinn', 'Rodriguez');
insert into staff values (20, 'Catering', 'G8', 'Ryan', 'Rodriguez');
insert into staff values (21, 'Pilot', 'VA', 'Sara', 'Hernandez');
insert into staff values (22, 'Cabin Crew', 'VA', 'Tom', 'Hernandez');
insert into staff values (23, 'Ground Staff', 'VA', 'Uma', 'Gonzalez');
insert into staff values (24, 'Security', 'VA', 'Vince', 'Gonzalez');
insert into staff values (25, 'Catering', 'VA', 'Wendy', 'Torres');
insert into staff values (26, 'Pilot', '6E', 'Xavier', 'Torres');
insert into staff values (27, 'Cabin Crew', '6E', 'Yara', 'Ramirez');
insert into staff values (28, 'Ground Staff', '6E', 'Zack', 'Ramirez');
insert into staff values (29, 'Security', '6E', 'Ava', 'Sanchez');
insert into staff values (30, 'Catering', '6E', 'Ben', 'Sanchez');
insert into staff values (31, 'Pilot', 'AI', 'Cara', 'Brown');
insert into staff values (32, 'Cabin Crew', 'AI', 'Dale', 'Brown');
insert into staff values (33, 'Ground Staff', 'AI', 'Ella', 'Johnson');
insert into staff values (34, 'Security', 'AI', 'Finn', 'Johnson');
insert into staff values (35, 'Catering', 'AI', 'Gina', 'Williams');
insert into staff values (36, 'Pilot', 'SJ', 'Hank', 'Williams');
insert into staff values (37, 'Cabin Crew', 'SJ', 'Iris', 'Martinez');
insert into staff values (38, 'Ground Staff', 'SJ', 'Jack', 'Martinez');
insert into staff values (39, 'Security', 'SJ', 'Kara', 'Garcia');
insert into staff values (40, 'Catering', 'SJ', 'Liam', 'Garcia');
insert into staff values (41, 'Pilot', 'G8', 'Mia', 'Lopez');
insert into staff values (42, 'Cabin Crew', 'G8', 'Nate', 'Lopez');
insert into staff values (43, 'Ground Staff', 'G8', 'Olivia', 'Perez');
insert into staff values (44, 'Security', 'G8', 'Pete', 'Perez');
insert into staff values (45, 'Catering', 'G8', 'Quinn', 'Rodriguez');
insert into staff values (46, 'Pilot', 'VA', 'Ryan', 'Rodriguez');
insert into staff values (47, 'Cabin Crew', 'VA', 'Sara', 'Hernandez');
insert into staff values (48, 'Ground Staff', 'VA', 'Tom', 'Hernandez');
insert into staff values (49, 'Security', 'VA', 'Uma', 'Gonzalez');
insert into staff values (50, 'Catering', 'VA', 'Vince', 'Gonzalez');

-- 50 inventory items, 5 types, 10 of each type
insert into inventory values (1, 'Pushback Tug');
insert into inventory values (2, 'Catering Truck');
insert into inventory values (3, 'Fuel Truck');
insert into inventory values (4, 'Baggage Cart');
insert into inventory values (5, 'Passenger Bus');
insert into inventory values (6, 'Pushback Tug');
insert into inventory values (7, 'Catering Truck');
insert into inventory values (8, 'Fuel Truck');
insert into inventory values (9, 'Baggage Cart');
insert into inventory values (10, 'Passenger Bus');
insert into inventory values (11, 'Pushback Tug');
insert into inventory values (12, 'Catering Truck');
insert into inventory values (13, 'Fuel Truck');
insert into inventory values (14, 'Baggage Cart');
insert into inventory values (15, 'Passenger Bus');
insert into inventory values (16, 'Pushback Tug');
insert into inventory values (17, 'Catering Truck');
insert into inventory values (18, 'Fuel Truck');
insert into inventory values (19, 'Baggage Cart');
insert into inventory values (20, 'Passenger Bus');
insert into inventory values (21, 'Pushback Tug');
insert into inventory values (22, 'Catering Truck');
insert into inventory values (23, 'Fuel Truck');
insert into inventory values (24, 'Baggage Cart');
insert into inventory values (25, 'Passenger Bus');
insert into inventory values (26, 'Pushback Tug');
insert into inventory values (27, 'Catering Truck');
insert into inventory values (28, 'Fuel Truck');
insert into inventory values (29, 'Baggage Cart');
insert into inventory values (30, 'Passenger Bus');
insert into inventory values (31, 'Pushback Tug');
insert into inventory values (32, 'Catering Truck');
insert into inventory values (33, 'Fuel Truck');
insert into inventory values (34, 'Baggage Cart');
insert into inventory values (35, 'Passenger Bus');
insert into inventory values (36, 'Pushback Tug');
insert into inventory values (37, 'Catering Truck');
insert into inventory values (38, 'Fuel Truck');
insert into inventory values (39, 'Baggage Cart');
insert into inventory values (40, 'Passenger Bus');
insert into inventory values (41, 'Pushback Tug');
insert into inventory values (42, 'Catering Truck');
insert into inventory values (43, 'Fuel Truck');
insert into inventory values (44, 'Baggage Cart');
insert into inventory values (45, 'Passenger Bus');
insert into inventory values (46, 'Pushback Tug');
insert into inventory values (47, 'Catering Truck');
insert into inventory values (48, 'Fuel Truck');
insert into inventory values (49, 'Baggage Cart');
insert into inventory values (50, 'Passenger Bus');

-- 50 ground assets, 5 per flight, 1 of each type per flight
insert into ground_assets values (1, 1);
insert into ground_assets values (2, 1);
insert into ground_assets values (3, 1);
insert into ground_assets values (4, 1);
insert into ground_assets values (5, 1);
insert into ground_assets values (6, 2);
insert into ground_assets values (7, 2);
insert into ground_assets values (8, 2);
insert into ground_assets values (9, 2);
insert into ground_assets values (10, 2);
insert into ground_assets values (11, 3);
insert into ground_assets values (12, 3);
insert into ground_assets values (13, 3);
insert into ground_assets values (14, 3);
insert into ground_assets values (15, 3);
insert into ground_assets values (16, 4);
insert into ground_assets values (17, 4);
insert into ground_assets values (18, 4);
insert into ground_assets values (19, 4);
insert into ground_assets values (20, 4);
insert into ground_assets values (21, 5);
insert into ground_assets values (22, 5);
insert into ground_assets values (23, 5);
insert into ground_assets values (24, 5);
insert into ground_assets values (25, 5);
insert into ground_assets values (26, 6);
insert into ground_assets values (27, 6);
insert into ground_assets values (28, 6);
insert into ground_assets values (29, 6);
insert into ground_assets values (30, 6);
insert into ground_assets values (31, 7);
insert into ground_assets values (32, 7);
insert into ground_assets values (33, 7);
insert into ground_assets values (34, 7);
insert into ground_assets values (35, 7);
insert into ground_assets values (36, 8);
insert into ground_assets values (37, 8);
insert into ground_assets values (38, 8);
insert into ground_assets values (39, 8);
insert into ground_assets values (40, 8);
insert into ground_assets values (41, 9);
insert into ground_assets values (42, 9);
insert into ground_assets values (43, 9);
insert into ground_assets values (44, 9);
insert into ground_assets values (45, 9);
insert into ground_assets values (46, 10);
insert into ground_assets values (47, 10);
insert into ground_assets values (48, 10);
insert into ground_assets values (49, 10);
insert into ground_assets values (50, 10);

-- 5 shops
insert into shops values (1, 'Duty Free', 1, 1, 'Retail');
insert into shops values (2, 'The Book Store', 1, 2, 'Retail');
insert into shops values (3, 'The Electronics Store', 1, 2, 'Electronics');
insert into shops values (4, 'The Fashion Store', 2, 1, 'Retail');
insert into shops values (5, 'Food Court', 2, 1, 'Food');

-- 10 orders placed by passengers, 2 orders each for 5 passengers
insert into shopping values (1, 1, 1, 1000);
insert into shopping values (2, 2, 2, 2000);
insert into shopping values (3, 3, 3, 3000);
insert into shopping values (4, 4, 4, 4000);
insert into shopping values (5, 5, 5, 5000);
insert into shopping values (6, 1, 6, 6000);
insert into shopping values (7, 2, 7, 7000);
insert into shopping values (8, 3, 8, 8000);
insert into shopping values (9, 4, 9, 9000);
insert into shopping values (10, 5, 10, 10000);

-- 85 passengers with only one bag
-- 4 passengers with 2 bags
-- 1 passenger with 3 bags
-- 10 passengers with no bags
insert into bags values (1, 1, 10, true, false, false, 1, false);
insert into bags values (2, 2, 20, true, false, false, 2, false);
insert into bags values (3, 3, 30, true, false, false, 3, false);
insert into bags values (4, 4, 10, true, false, false, 4, false);
insert into bags values (5, 5, 20, true, false, false, 5, false);
insert into bags values (6, 6, 30, true, false, false, 6, false);
insert into bags values (7, 7, 10, true, false, false, 7, false);
insert into bags values (8, 8, 20, true, false, false, 8, false);
insert into bags values (9, 9, 30, true, false, false, 9, false);
insert into bags values (10, 10, 10, true, false, false, 10, false);
insert into bags values (11, 11, 20, true, false, false, 1, false);
insert into bags values (12, 12, 30, true, false, false, 2, false);
insert into bags values (13, 13, 10, true, false, false, 3, false);
insert into bags values (14, 14, 20, true, false, false, 4, false);
insert into bags values (15, 15, 30, true, false, false, 5, false);
insert into bags values (16, 16, 10, true, false, false, 6, false);
insert into bags values (17, 17, 20, true, false, false, 7, false);
insert into bags values (18, 18, 30, true, false, false, 8, false);
insert into bags values (19, 19, 10, true, false, false, 9, false);
insert into bags values (20, 20, 20, true, false, false, 10, false);
insert into bags values (21, 21, 30, true, false, false, 1, false);
insert into bags values (22, 22, 10, true, false, false, 2, false);
insert into bags values (23, 23, 20, true, false, false, 3, false);
insert into bags values (24, 24, 30, true, false, false, 4, false);
insert into bags values (25, 25, 10, true, false, false, 5, false);
insert into bags values (26, 26, 20, true, false, false, 6, false);
insert into bags values (27, 27, 30, true, false, false, 7, false);
insert into bags values (28, 28, 10, true, false, false, 8, false);
insert into bags values (29, 29, 20, true, false, false, 9, false);
insert into bags values (30, 30, 30, true, false, false, 10, false);
insert into bags values (31, 31, 10, true, false, false, 1, false);
insert into bags values (32, 32, 20, true, false, false, 2, false);
insert into bags values (33, 33, 30, true, false, false, 3, false);
insert into bags values (34, 34, 10, true, false, false, 4, false);
insert into bags values (35, 35, 20, true, false, false, 5, false);
insert into bags values (36, 36, 30, true, false, false, 6, false);
insert into bags values (37, 37, 10, true, false, false, 7, false);
insert into bags values (38, 38, 20, true, false, false, 8, false);
insert into bags values (39, 39, 30, true, false, false, 9, false);
insert into bags values (40, 40, 10, true, false, false, 10, false);
insert into bags values (41, 41, 20, true, false, false, 1, false);
insert into bags values (42, 42, 30, true, false, false, 2, false);
insert into bags values (43, 43, 10, true, false, false, 3, false);
insert into bags values (44, 44, 20, true, false, false, 4, false);
insert into bags values (45, 45, 30, true, false, false, 5, false);
insert into bags values (46, 46, 10, true, false, false, 6, false);
insert into bags values (47, 47, 20, true, false, false, 7, false);
insert into bags values (48, 48, 30, true, false, false, 8, false);
insert into bags values (49, 49, 10, true, false, false, 9, false);
insert into bags values (50, 50, 20, true, false, false, 10, false);
insert into bags values (51, 51, 30, true, false, false, 1, false);
insert into bags values (52, 52, 10, true, false, false, 2, false);
insert into bags values (53, 53, 20, true, false, false, 3, false);
insert into bags values (54, 54, 30, true, false, false, 4, false);
insert into bags values (55, 55, 10, true, false, false, 5, false);
insert into bags values (56, 56, 20, true, false, false, 6, false);
insert into bags values (57, 57, 30, true, false, false, 7, false);
insert into bags values (58, 58, 10, true, false, false, 8, false);
insert into bags values (59, 59, 20, true, false, false, 9, false);
insert into bags values (60, 60, 30, true, false, false, 10, false);
insert into bags values (61, 61, 10, true, false, false, 1, false);
insert into bags values (62, 62, 20, true, false, false, 2, false);
insert into bags values (63, 63, 30, true, false, false, 3, false);
insert into bags values (64, 64, 10, true, false, false, 4, false);
insert into bags values (65, 65, 20, true, false, false, 5, false);
insert into bags values (66, 66, 30, true, false, false, 6, false);
insert into bags values (67, 67, 10, true, false, false, 7, false);
insert into bags values (68, 68, 20, true, false, false, 8, false);
insert into bags values (69, 69, 30, true, false, false, 9, false);
insert into bags values (70, 70, 10, true, false, false, 10, false);
insert into bags values (71, 71, 20, true, false, false, 1, false);
insert into bags values (72, 72, 30, true, false, false, 2, false);
insert into bags values (73, 73, 10, true, false, false, 3, false);
insert into bags values (74, 74, 20, true, false, false, 4, false);
insert into bags values (75, 75, 30, true, false, false, 5, false);
insert into bags values (76, 76, 10, true, false, false, 6, false);
insert into bags values (77, 77, 20, true, false, false, 7, false);
insert into bags values (78, 78, 30, true, false, false, 8, false);
insert into bags values (79, 79, 10, true, false, false, 9, false);
insert into bags values (80, 80, 20, true, false, false, 10, false);
insert into bags values (81, 81, 30, true, false, false, 1, false);
insert into bags values (82, 82, 10, true, false, false, 2, false);
insert into bags values (83, 83, 20, true, false, false, 3, false);
insert into bags values (84, 84, 30, true, false, false, 4, false);
insert into bags values (85, 85, 10, true, false, false, 5, false);
insert into bags values (86, 86, 20, true, false, false, 6, false);
insert into bags values (87, 87, 30, true, false, false, 7, false);
insert into bags values (88, 88, 10, true, false, false, 8, false);
insert into bags values (89, 89, 20, true, false, false, 8, false);
insert into bags values (90, 90, 30, true, false, false, 10, false);
insert into bags values (91, 86, 10, true, false, false, 6, false);
insert into bags values (92, 87, 20, true, false, false, 7, false);
insert into bags values (93, 88, 30, true, false, false, 8, false);
insert into bags values (94, 89, 10, true, false, false, 9, false);
insert into bags values (95, 90, 20, true, false, false, 10, false);
insert into bags values (96, 90, 30, true, false, false, 10, false);