CREATE DATABASE IF NOT EXISTS airportdb;

USE airportdb;

CREATE TABLE IF NOT EXISTS passengers (
    Pass_ID INT AUTO_INCREMENT PRIMARY KEY,
    First_name VARCHAR(255),
    Last_name VARCHAR(255),
    DOB DATE,
    Flight_No INT,
    Privilege BOOLEAN,
    Mobile_No INT, 
    Red_Flag BOOLEAN,
    FOREIGN KEY (Flight_No) REFERENCES flights(Flight_No) ON DELETE CASCADE
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
    FOREIGN KEY (IATA_Code) REFERENCES airlines(IATA_Code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS airlines (
    IATA_Code VARCHAR(255) PRIMARY KEY,
    Airline_Name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS bags (
    Bag_ID INT AUTO_INCREMENT PRIMARY KEY,
    Pass_ID INT,
    Weight FLOAT,
    Is_Secure BOOLEAN,
    Is_Fragile BOOLEAN,
    Priority BOOLEAN,
    Status VARCHAR(255),
    Lost BOOLEAN,
    FOREIGN KEY (Pass_ID) REFERENCES passengers(Pass_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS staff (
    Staff_ID INT AUTO_INCREMENT PRIMARY KEY,
    Role VARCHAR(255),
    IATA_Code VARCHAR(255),
    First_name VARCHAR(255),
    Last_name VARCHAR(255),
    FOREIGN KEY (IATA_Code) REFERENCES airlines(IATA_Code) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ground_assets (
    I_ID INT,
    Flight_No INT,
    PRIMARY KEY (I_ID, Flight_No),
    FOREIGN KEY (I_ID) REFERENCES inventory(I_ID) ON DELETE CASCADE,
    FOREIGN KEY (Flight_No) REFERENCES flights(Flight_No) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS inventory (
    I_ID INT AUTO_INCREMENT PRIMARY KEY,
    Type VARCHAR(255)
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
)