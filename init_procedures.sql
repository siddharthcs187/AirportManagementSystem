use airportdb;

-- display staff details
DELIMITER //

CREATE PROCEDURE ViewStaffByIATA()
BEGIN
    -- Query to view staff details grouped by IATA code
    SELECT 
        Staff_ID AS ID,
        CONCAT(First_name, ' ', Last_name) AS Name,
        IATA_Code AS IATA 
    FROM 
        staff 
    GROUP BY 
        Staff_ID, IATA_Code;

    SELECT 'Staff details grouped by IATA code retrieved successfully.' AS Message;
END //

DELIMITER ;

-- show inventory and count
DELIMITER //

CREATE PROCEDURE ViewInventoryCountByType()
BEGIN
    -- Query to view inventory count grouped by type
    SELECT 
        Type,
        COUNT(I_ID) as Count
    FROM 
        inventory 
    GROUP BY 
        Type;

    SELECT 'Inventory count grouped by type retrieved successfully.' AS Message;
END //

DELIMITER ;

-- show which vehicles are assigned to which airline
DELIMITER //

CREATE PROCEDURE GetGroundAssets()
BEGIN
    -- Query to retrieve ground assets with vehicle type and airline
    SELECT 
        ga.*,
        i.Type AS `Vehicle Type`,
        a.Airline_Name AS `Airline`
    FROM 
        ground_assets ga
    JOIN 
        inventory i ON ga.I_ID = i.I_ID
    JOIN
        flights f ON ga.Flight_No = f.Flight_No
    JOIN
        airlines a ON f.IATA_Code = a.IATA_Code;

    SELECT 'Ground assets retrieved successfully.' AS Message;
END //

DELIMITER ;

-- selecting incoming flights
DELIMITER //

CREATE PROCEDURE IncomingFlights()
BEGIN
    SELECT 
        Flight_No AS `Flight Number`, 
        Date_Time AS `Date and Time`, 
        Origin AS `From`, 
        Destination AS `To`, 
        Baggage_Belt AS `Baggage Belt`,
        IATA_Code AS `Airline`,
        Status AS `Status`
    FROM 
        flights 
    WHERE 
        Is_arriving = 1
    ORDER BY 
        Date_Time;
    SELECT 'Incoming flights retrieved successfully.' AS Message;
END //

DELIMITER ;

-- selecting outgoing flights
DELIMITER //

CREATE PROCEDURE ViewOutgoingFlights()
BEGIN
    -- Query to view outgoing flights
    SELECT 
        Flight_No as "Flight Number", 
        Date_Time as "Date and Time", 
        Origin as "From", 
        Destination as "To", 
        Checkin_Lane as "Check-In Lane", 
        IATA_Code as "Airline",
        Status as "Status"
    FROM 
        flights 
    WHERE 
        Is_arriving = 0
    ORDER BY 
        Date_Time;
	SELECT 'Outgoing flights retrieved successfully.' AS Message;
END //

DELIMITER ;

-- select all airlines
DELIMITER //

CREATE PROCEDURE GetAirlines()
BEGIN
    SELECT 
        IATA_Code AS `IATA CODE`, 
        Airline_Name AS Airline 
    FROM 
        airlines 
    ORDER BY 
        Airline_Name;

    SELECT 'Airlines retrieved successfully.' AS Message;
END //

DELIMITER ;

-- select all passengers

DELIMITER //

CREATE PROCEDURE GetAllPassengers()
BEGIN    
    SELECT 
        Pass_ID AS `Passenger ID`,
        CONCAT(First_name, ' ', Last_name) AS Name, 
        CAST(DATEDIFF(CURRENT_DATE(), DOB)/365 AS UNSIGNED) AS Age, 
        Flight_No AS `Flight Number`, 
        Privilege,
        Mobile_No AS `Mobile Number`, 
        Red_Flag AS `Red Flag` 
    FROM 
        passengers;

    SELECT 'Passengers retrieved successfully.' AS Message;
END //
DELIMITER ;

-- selects all bags
DELIMITER //

CREATE PROCEDURE GetAllBags()
BEGIN
    SELECT 
        Bag_ID AS `Bag ID`, 
        Pass_ID AS `Passenger ID`, 
        Weight, 
        Is_Secure AS `Is Secure`, 
        Is_Fragile AS `Is Fragile`, 
        Flight_No AS `Flight Number`, 
        Lost 
    FROM 
        bags;
    SELECT 'Bags retrieved successfully.' AS Message;
END //

DELIMITER ;

-- security personnel flagging a bag
DELIMITER //

CREATE PROCEDURE FlagBag(IN p_Bag_ID INT)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;
    
    -- Update the Is_Secure status of the bag
    UPDATE bags
    SET Is_Secure = false
    WHERE Bag_ID = p_Bag_ID;

    COMMIT;
    SELECT 'Bag flagged successfully.' AS Message;
END //

DELIMITER ;

-- security personnel UNflagging a bag
DELIMITER //

CREATE PROCEDURE unFlagBag(IN p_Bag_ID INT)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;
    
    -- Update the Is_Secure status of the bag
    UPDATE bags
    SET Is_Secure = true
    WHERE Bag_ID = p_Bag_ID;

    COMMIT;
    SELECT 'Bag flagged successfully.' AS Message;
END //

DELIMITER ;

-- security personnel flagging a person
DELIMITER //

CREATE PROCEDURE FlagPassenger(IN p_Pass_ID INT)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    -- Update the Red_Flag status of the passenger
    UPDATE passengers
    SET Red_Flag = true
    WHERE Pass_ID = p_Pass_ID;

    COMMIT;
    SELECT 'Passenger flagged successfully.' AS Message;
END //

DELIMITER ;

-- security personnel unflagging a person
DELIMITER //

CREATE PROCEDURE unFlagPassenger(IN p_Pass_ID INT)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    -- Update the Red_Flag status of the passenger
    UPDATE passengers
    SET Red_Flag = false
    WHERE Pass_ID = p_Pass_ID;

    COMMIT;
    SELECT 'Passenger flagged successfully.' AS Message;
END //

DELIMITER ;

-- admin screen to check all suspicious baggage
DELIMITER //

CREATE PROCEDURE GetInsecureBags()
BEGIN
    DECLARE exit handler for sqlexception
        BEGIN
            ROLLBACK;
            SELECT 'Error occurred, transaction rolled back.' AS Message;
        END;

    START TRANSACTION;
    
    SELECT 
        Bag_ID AS `Bag ID`,
        Pass_ID AS `Passenger ID`,
        Flight_No AS `Flight Number`
    FROM 
        bags 
    WHERE 
        Is_Secure = false;
    
    COMMIT;
    
    SELECT 'Insecure bags retrieved successfully.' AS Message;
END //

DELIMITER ;

-- admin screen to check suspicious passengers
DELIMITER //

CREATE PROCEDURE GetPassengersWithRedFlag()
BEGIN
    DECLARE exit handler for sqlexception
        BEGIN
            ROLLBACK;
            SELECT 'Error occurred, transaction rolled back.' AS Message;
        END;

    START TRANSACTION;
    
    SELECT 
        Pass_ID AS `Passenger ID`, 
        CONCAT(First_name, ' ', Last_name) AS Name, 
        CAST(DATEDIFF(CURRENT_DATE(), DOB)/365 AS UNSIGNED) AS Age, 
        Flight_No AS `Flight Number`
    FROM 
        passengers 
    WHERE 
        Red_Flag = true;
    
    COMMIT;
    
    SELECT 'Passengers with red flag retrieved successfully.' AS Message;
END //

DELIMITER ;

-- select shops and sum of orders placed
DELIMITER //

CREATE PROCEDURE GetSales()
BEGIN
    DECLARE exit handler for sqlexception
        BEGIN
            ROLLBACK;
            SELECT 'Error occurred, transaction rolled back.' AS Message;
        END;

    START TRANSACTION;
    
    SELECT 
        s.Shop_ID AS `Shop ID`, 
        s.Name AS Name, 
        SUM(sh.Amount) AS Sales 
    FROM 
        shops s 
    INNER JOIN 
        shopping sh ON s.Shop_ID = sh.Shop_ID 
    GROUP BY 
        s.Shop_ID;
    
    COMMIT;
    
    SELECT 'Sales data retrieved successfully.' AS Message;
END //

DELIMITER ;

-- Add an incoming flight to the system

-- Adding a flight
DELIMITER //

CREATE PROCEDURE AddFlightAndAirline(
    IN p_IATA_Code VARCHAR(255),
    IN p_Airline_Name VARCHAR(255),
    IN p_Date_Time DATETIME,
    IN p_Is_Arriving BOOLEAN,
    IN p_Origin VARCHAR(255),
    IN p_Destination VARCHAR(255),
    IN p_Baggage_Belt INT,
    IN p_Checkin_Lane INT,
    IN p_Status VARCHAR(30)
)
BEGIN
    DECLARE airline_exists INT;

    DECLARE exit handler for sqlexception
        BEGIN
            ROLLBACK;
            SELECT 'Error occurred, transaction rolled back.' AS Message;
        END;

    START TRANSACTION;
    
    -- Check if the airline exists
    SELECT COUNT(*) INTO airline_exists FROM airlines WHERE IATA_Code = p_IATA_Code;
    
    -- If airline doesn't exist, add it
    IF airline_exists = 0 THEN
        INSERT INTO airlines (IATA_Code, Airline_Name) VALUES (p_IATA_Code, p_Airline_Name);
    END IF;

    -- Add the flight
    INSERT INTO flights (Date_Time, Is_Arriving, Gate, Origin, Destination, Baggage_Belt, Checkin_Lane, IATA_Code, Status)
    VALUES (p_Date_Time, p_Is_Arriving, NULL, p_Origin, p_Destination, p_Baggage_Belt, p_Checkin_Lane, p_IATA_Code, p_Status);

    COMMIT;
    SELECT 'Flight added.' AS Message;
END //

DELIMITER ;

-- add  single passenger
DELIMITER //

CREATE PROCEDURE AddPassengerOnFlight(
    IN p_First_name VARCHAR(255),
    IN p_Last_name VARCHAR(255),
    IN p_DOB DATE,
    IN p_Flight_No INT,
    IN p_Privilege VARCHAR(30),
    IN p_Mobile_No VARCHAR(10)
)
BEGIN
    DECLARE exit handler for sqlexception
        BEGIN
            ROLLBACK;
            SELECT 'Error occurred, transaction rolled back.' AS Message;
        END;

    START TRANSACTION;
    -- Add the passenger
    INSERT INTO passengers (First_name, Last_name, DOB, Flight_No, Privilege, Mobile_No, Red_Flag)
    VALUES (p_First_name, p_Last_name, p_DOB, p_Flight_No, p_Privilege, p_Mobile_No, 0);
    COMMIT;
    SELECT 'Passenger added' AS Message;
END //

DELIMITER ;

-- add a bag

DELIMITER //

CREATE PROCEDURE AddBag(
    IN p_Pass_ID INT,
    IN p_Weight FLOAT,
    IN p_Is_Fragile BOOLEAN,
    IN p_Priority BOOLEAN,
    IN p_Flight_No INT
)
BEGIN
    DECLARE exit handler for sqlexception
        BEGIN
            ROLLBACK;
            SELECT 'Error occurred, transaction rolled back.' AS Message;
        END;

    START TRANSACTION;
    -- Add the bag
    INSERT INTO bags (Pass_ID, Weight, Is_Secure, Is_Fragile, Priority, Flight_No, Lost)
    VALUES (p_Pass_ID, p_Weight, 1, p_Is_Fragile, p_Priority, p_Flight_No, 0);

    COMMIT;
    SELECT 'Passenger added' AS Message;
END //

DELIMITER ;

-- airline view for their flights
DELIMITER //

CREATE PROCEDURE GetPassengerInfoByAirline(IN airline_code VARCHAR(10))
BEGIN
    SELECT 
        p.Flight_No AS `Flight Number`, 
        p.Pass_ID AS `Passenger ID`, 
        CONCAT(p.First_name, ' ', p.Last_name) AS Name, 
        CAST(DATEDIFF(CURRENT_DATE(), DOB)/365 AS UNSIGNED) AS Age
    FROM 
        passengers p 
    INNER JOIN 
        bags b ON p.Pass_ID = b.Pass_ID 
    INNER JOIN 
        flights f ON p.Flight_No = f.Flight_No 
    WHERE 
        f.IATA_Code = airline_code
    GROUP BY 
        p.Flight_No, p.Pass_ID, Name, Age
    ORDER BY 
        p.Flight_No, p.First_name;
        
	SELECT 'Airline passenger information retrieved successfully.' AS Message;
END //

DELIMITER ;

-- airline info of which flight uses what belt/ check in lane
DELIMITER //

CREATE PROCEDURE GetBeltAndLaneInfo(IN airline_code VARCHAR(10))
BEGIN
    SELECT 
        Flight_No AS `Flight Number`, 
        CASE WHEN Baggage_Belt = -1 THEN '-' ELSE CAST(Baggage_Belt AS CHAR) END AS `Baggage Belt`, 
        CASE WHEN Checkin_Lane = -1 THEN '-' ELSE CAST(Checkin_Lane AS CHAR) END AS `Check-In Lane` 
    FROM 
        flights 
    WHERE 
        IATA_Code = airline_code;
	SELECT 'Airline baggage belt and check in lane information retrieved successfully.' AS Message;
END //

DELIMITER ;

-- airline can update its flight status
DELIMITER //

CREATE PROCEDURE SetStatus(IN flight_id INT, IN stat VARCHAR(255))
BEGIN
DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;


    START TRANSACTION;
        UPDATE flights
        SET Status = stat
        WHERE Flight_No = flight_id;
    COMMIT;
END //

DELIMITER ;

-- add/remove staff for an airline
DELIMITER //

CREATE PROCEDURE AddStaff(
    IN p_Role VARCHAR(255),
    IN p_AirlineIATA VARCHAR(255),
    IN p_FirstName VARCHAR(255),
    IN p_LastName VARCHAR(255)
)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    INSERT INTO staff (Role, IATA_Code, First_name, Last_name)
    VALUES (p_Role, p_AirlineIATA, p_FirstName, p_LastName);

    COMMIT;
    SELECT 'Staff added successfully.' AS Message;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE RemoveStaff(
    IN p_StaffID INT,
    IN p_AirlineIATA VARCHAR(255)
)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    DELETE FROM staff WHERE Staff_ID = p_StaffID AND IATA_Code = p_AirlineIATA;

    COMMIT;
    SELECT 'Staff removed successfully.' AS Message;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE AddEquipment(
    IN p_FlightNo INT,
    IN p_Type VARCHAR(255)
)
BEGIN
    DECLARE last_id INT DEFAULT 0;
	
	DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;
    
    START TRANSACTION;
	
     -- Insert into inventory without specifying I_ID
    INSERT INTO inventory (Type)
    VALUES (p_Type);

    -- Get the last inserted I_ID from ground_assets
    SELECT LAST_INSERT_ID() INTO last_id;

    INSERT INTO ground_assets (I_ID, Flight_No)
    VALUES (last_id, p_FlightNo);

    COMMIT;
    SELECT 'Equipment added successfully.' AS Message;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE RemoveEquipment(
    IN p_EquipmentID INT
)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    DELETE FROM ground_assets WHERE I_ID = p_EquipmentID;

    DELETE FROM inventory WHERE I_ID = p_EquipmentID;

    COMMIT;
    SELECT 'Equipment removed successfully.' AS Message;
END //

DELIMITER ;

-- airline view of red flagged passengers
DELIMITER //
CREATE PROCEDURE ViewRedFlaggedPassengersByAirline(
    IN p_AirlineIATA VARCHAR(255)
)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    -- Query to view red-flagged passengers for the given airline
    SELECT 
        p.Pass_ID AS "Passenger ID",
        CONCAT(p.First_name, ' ', p.Last_name) AS Name,
        p.DOB AS "Date of Birth",
        f.Flight_No AS "Flight Number",
        p.Privilege,
        p.Mobile_No AS "Mobile Number",
        p.Red_Flag AS "Red Flag"
    FROM 
        passengers p
    INNER JOIN 
        flights f ON p.Flight_No = f.Flight_No
    WHERE 
        p.Red_Flag = true
    AND
        f.IATA_Code = p_AirlineIATA
    ORDER BY 
        p.Pass_ID;

    COMMIT;
    SELECT 'Red-flagged passengers query executed successfully.' AS Message;
END //

DELIMITER ;

DELIMITER //

-- airline view of unsecure bags

CREATE PROCEDURE ViewUnsecureBagsByAirline(
    IN p_AirlineIATA VARCHAR(255)
)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    -- Query to view unsecure bags for the given airline
    SELECT 
        b.Bag_ID AS "Bag ID",
        CONCAT(p.First_name, ' ', p.Last_name) AS "Passenger Name",
        f.Flight_No AS "Flight Number",
        b.Weight,
        b.Is_Fragile AS "Is Fragile",
        b.Priority AS "Priority",
        b.Lost,
        b.Is_Secure AS "Is Secure"
    FROM 
        bags b
    INNER JOIN 
        passengers p ON b.Pass_ID = p.Pass_ID
    INNER JOIN 
        flights f ON b.Flight_No = f.Flight_No
    WHERE 
        b.Is_Secure = false
    AND
        f.IATA_Code = p_AirlineIATA
    ORDER BY 
        b.Bag_ID;

    COMMIT;
    SELECT 'Unsecure bags query executed successfully.' AS Message;
END //

DELIMITER ;

-- check for passengers with overweight bags for a given airline
DELIMITER //

CREATE PROCEDURE GetExcessBaggage(IN IATA VARCHAR(255))
BEGIN
    SELECT 
    b.Bag_ID,
    b.Pass_ID,
    b.Weight - CASE
                    WHEN p.Privilege = 'first class' THEN 30
                    WHEN p.Privilege = 'business'  THEN 25
                    ELSE 20
               END AS Excess_Weight,
    CASE
        WHEN p.Privilege = 'first class'  THEN (b.Weight - 30) * 200
        WHEN p.Privilege = 'business' THEN (b.Weight - 25) * 300
        ELSE (b.Weight - 20) * 500
    END AS Excess_Charges
FROM 
    bags b
INNER JOIN 
    passengers p ON b.Pass_ID = p.Pass_ID
INNER JOIN 
	flights f ON p.Flight_No = f.Flight_No
WHERE
	f.IATA_Code = IATA and
    b.Weight > CASE
                    WHEN p.Privilege = 'first class' THEN 30
                    WHEN p.Privilege = 'business' THEN 25
                    ELSE 20
               END
ORDER BY b.Bag_ID;
SELECT 'Overweight baggage data received.' AS Message;
END //

DELIMITER ;

-- allow airlines to give privileges to passengers
DELIMITER //

CREATE PROCEDURE ChangePrivilege(IN pid INT, IN priv VARCHAR(255))
BEGIN
	DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;
    START TRANSACTION;
        UPDATE passengers
        SET Privilege = priv
        WHERE Pass_ID = pid;
    COMMIT;
	SELECT 'Privileges applied.' AS Message;
END //

DELIMITER ;

-- show passnegers with lost bags

DELIMITER //

CREATE PROCEDURE ViewLostBagsByAirline(IN p_IATA_Code VARCHAR(255))
BEGIN
    -- Query to view lost bags for a given airline
    SELECT 
        b.*,
        CONCAT(p.First_name, ' ', p.Last_name) AS Passenger_Name,
        f.Flight_No AS Flight_Number,
        f.Origin AS From_Location,
        f.Destination AS To_Location
    FROM 
        bags b
    JOIN 
        passengers p ON b.Pass_ID = p.Pass_ID
    JOIN 
        flights f ON b.Flight_No = f.Flight_No
    WHERE 
        b.Lost = true
        AND f.IATA_Code = p_IATA_Code;

    SELECT 'Lost bags for the airline retrieved successfully.' AS Message;
END //

DELIMITER ;

-- display shops info for passengers
DELIMITER //

CREATE PROCEDURE GetShops()
BEGIN
    SELECT 
        Name, 
        Terminal_No AS Terminal, 
        Floor_No AS Floor, 
        Category 
    FROM 
        shops 
    ORDER BY 
        Terminal_No, Floor_No;
	SELECT 'Shops displayed.' AS Message;
END //

DELIMITER ;

-- Place orders in shops
DELIMITER //

CREATE PROCEDURE InsertShoppingOrder(
    IN p_Shop_ID INT,
    IN p_Pass_ID INT,
    IN p_Amount INT
)
BEGIN
	DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;
    START TRANSACTION;
		INSERT INTO shopping (Shop_ID, Pass_ID, Amount)
		VALUES (p_Shop_ID, p_Pass_ID, p_Amount);
	COMMIT;
    SELECT "Order placed." AS Message;
END //

DELIMITER ;

-- Listing a bag as lost 
DELIMITER //

CREATE PROCEDURE LostBag(IN p_Bag_ID INT)
BEGIN
	DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;
    START TRANSACTION;
        UPDATE bags
        SET Lost = 1
        WHERE Bag_ID = p_Bag_ID;
    COMMIT;
	SELECT "Bag listed as lost." AS Message;
END //

DELIMITER //

-- search query for flights

DELIMITER //

CREATE PROCEDURE GetFlightDetails(IN flightNumber INT)
BEGIN
    SELECT 
        Flight_No AS `Flight Number`, 
        Date_Time AS `Date and Time`, 
        Origin AS `From`,
        Checkin_Lane AS `Check-In Lane`, 
        Destination AS `To`, 
        Baggage_Belt AS `Baggage Belt` 
    FROM 
        flights 
    WHERE 
        Flight_No = flightNumber;
END //

DELIMITER ;

-- search query for staff

DELIMITER //

CREATE PROCEDURE GetStaffDetails(IN namePlaceholder VARCHAR(255))
BEGIN
    SELECT 
        Staff_ID AS ID,
        CONCAT(First_name, ' ', Last_name) AS Name,
        IATA_Code AS IATA 
    FROM 
        staff 
    WHERE 
        First_name LIKE CONCAT(namePlaceholder, '%')
    ORDER BY 
        Staff_ID;
END //

DELIMITER ;

-- search query for bags

DELIMITER //

CREATE PROCEDURE GetBagDetails(IN bagID INT)
BEGIN
    SELECT 
        Bag_ID AS `Bag ID`, 
        Pass_ID AS `Passenger ID`, 
        Weight, 
        Is_Secure AS `Is Secure`, 
        Is_Fragile AS `Is Fragile`, 
        Flight_No AS `Flight Number`, 
        Lost 
    FROM 
        bags
    WHERE 
        Bag_ID = bagID;
END //

DELIMITER ;

-- search query for passengers

DELIMITER //

CREATE PROCEDURE GetPassengerDetails(IN namePlaceholder VARCHAR(255))
BEGIN
    SELECT 
        Pass_ID AS `Passenger ID`,
        CONCAT(First_name, ' ', Last_name) AS Name, 
        CAST(DATEDIFF(CURRENT_DATE(), DOB)/365 AS UNSIGNED) AS Age, 
        Flight_No AS `Flight Number`, 
        Privilege,
        Mobile_No AS `Mobile Number`, 
        Red_Flag AS `Red Flag` 
    FROM 
        passengers
    WHERE
        First_name LIKE CONCAT(namePlaceholder, '%')
    ORDER BY 
        First_name, Last_name;
END //

DELIMITER ;



