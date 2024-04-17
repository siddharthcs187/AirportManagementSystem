use airportdb;

-- display staff details
SELECT 
    Staff_ID AS ID,
    CONCAT(First_name, ' ', Last_name) AS Name,
    IATA_Code AS IATA 
FROM 
    staff 
GROUP BY 
    Staff_ID, IATA_Code;

-- show inventory and count
SELECT Type, count(I_ID) as Count FROM inventory GROUP BY Type;

-- show which vehicles are assigned to which airline
SELECT 
    ga.*,
    i.Type AS Vehicle_Type,
    a.Airline_Name
FROM 
    ground_assets ga
JOIN 
    inventory i ON ga.I_ID = i.I_ID
JOIN
    flights f ON ga.Flight_No = f.Flight_No
JOIN
    airlines a ON f.IATA_Code = a.IATA_Code;

-- selecting all flights
SELECT 
	Flight_No as "Flight Number", 
    Date_Time as "Date and Time", 
    Origin as "From", 
    Destination as "To", 
    Checkin_Lane as "Check-In Lane", 
    Baggage_Belt as "Baggage Belt",
    IATA_Code as "Airline",
    Status as "Status"
FROM flights 
ORDER BY Date_Time;

-- selecting incoming flights
SELECT 
	Flight_No as "Flight Number", 
    Date_Time as "Date and Time", 
    Origin as "From", 
    Destination as "To", 
    Baggage_Belt as "Baggage Belt",
    IATA_Code as "Airline",
    Status as "Status"
FROM flights 
WHERE Is_arriving = 1
ORDER BY Date_Time;

-- selecting outgoing flights
SELECT 
	Flight_No as "Flight Number", 
    Date_Time as "Date and Time", 
    Origin as "From", 
    Destination as "To", 
    Checkin_Lane as "Check-In Lane", 
    IATA_Code as "Airline",
    Status as "Status"
FROM flights 
WHERE Is_arriving = 0
ORDER BY Date_Time;

-- select all airlines
SELECT
IATA_Code as "IATA CODE", 
Airline_Name as Airline 
FROM airlines 
ORDER BY Airline_Name;

-- select all passengers
SELECT 
    Pass_ID as "Passenger ID",
    CONCAT(First_name, ' ', Last_name) AS Name, 
    CAST(DATEDIFF(CURRENT_DATE(), DOB)/365 AS UNSIGNED) as Age, 
    Flight_No as "Flight Number", 
    Privilege,
    Mobile_No as "Mobile Number", 
    Red_Flag as "Red Flag" 
FROM 
    passengers;

-- selects all bags

SELECT 
	Bag_ID as "Bag ID", 
	Pass_ID as "Passenger ID", 
	Weight, 
	Is_Secure as "Is Secure", 
	Is_Fragile as "Is Fragile", 
	Flight_No as "Flight Number", 
	Lost 
FROM bags;

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
--  unflagging a bag
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
    START TRANSACTION;
        UPDATE passengers
        SET Red_Flag = true
        WHERE Pass_ID = p_Pass_ID;
    COMMIT;
END //
DELIMITER ;

-- admin screen to check all suspicious baggage
Select 
	Bag_ID as "Bag ID",
	Pass_ID as "Passenger ID",
	Flight_No as "Flight Number" 
from bags 
where Is_Secure = false; 

-- admin screen to check suspicious passengers
Select 
	Pass_ID as "Passenger ID", 
	CONCAT(First_name, ' ', Last_name) AS Name, 
	CAST(DATEDIFF(CURRENT_DATE(), DOB)/365 AS UNSIGNED) as Age, 
	Flight_No as "Flight Number" 
from passengers 
where Red_Flag = true;

-- select shops and sum of orders placed
SELECT 
	s.Shop_ID as "Shop ID", 
	s.Name as Name, 
	SUM(sh.Amount) as Sales 
FROM shops s INNER JOIN shopping sh 
ON s.Shop_ID = sh.Shop_ID 
GROUP BY s.Shop_ID;

-- TODO : add an incoming flight to the system

-- airline view for their flights

SELECT 
    p.Flight_No as "Flight Number", 
    p.Pass_ID as "Passenger ID", 
    CONCAT(p.First_name, ' ', p.Last_name) AS Name, 
    DATEDIFF(CURRENT_DATE(), p.DOB)/365 as Age 
FROM 
    passengers p 
INNER JOIN 
    bags b ON p.Pass_ID = b.Pass_ID 
INNER JOIN 
    flights f ON p.Flight_No = f.Flight_No 
WHERE 
    f.IATA_Code = "<airlinecode>"
GROUP BY 
    p.Flight_No, p.Pass_ID, Name, Age  -- Include all non-aggregated columns in GROUP BY to avoid error 
ORDER BY 
    p.Flight_No, p.First_name;
    
-- airline info of which flight uses what belt/ check in lane
SELECT 
    Flight_No as "Flight Number", 
    CASE WHEN Baggage_Belt = -1 THEN '-' ELSE CAST(Baggage_Belt AS CHAR) END AS "Baggage Belt", 
    CASE WHEN Checkin_Lane = -1 THEN '-' ELSE CAST(Checkin_Lane AS CHAR) END AS "Check-In Lane" 
FROM 
    flights 
WHERE 
    IATA_Code = '<airlinecode>';

-- airline can update its flight status

DELIMITER //

CREATE PROCEDURE SetStatus(IN flight_id INT, IN stat VARCHAR(255))
BEGIN
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
    IN p_EquipmentID INT,
    IN p_FlightNo INT
)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back.' AS Message;
    END;

    START TRANSACTION;

    INSERT INTO ground_assets (I_ID, Flight_No)
    VALUES (p_EquipmentID, p_FlightNo);

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

    COMMIT;
    SELECT 'Equipment removed successfully.' AS Message;
END //

DELIMITER ;

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
















