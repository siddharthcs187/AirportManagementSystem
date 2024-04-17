# Airport Management System

The Airport Management System is a Java application designed to efficiently manage various aspects of airport operations including flights, passengers, staff, and inventory. It is built using Java, MySQL, and Swing.

Features

Admin Mode
Full System Overview with Search Functionality: Provides a comprehensive dashboard/interface for administrators to access information about baggage, flights, passengers, staff, and vehicles currently within the airport system.
Suspicious Baggage/Passenger Notification: Alerts administrators and relevant airlines when security personnel flag baggage or passengers as suspicious during screening.
Shop Information: Displays details about airport shops and their total revenue.
Adding Incoming Flights: Enables administrators to add incoming flights, including passengers and bags. New airlines can also be added, with the option to assign gates and check-in lanes.
Security Mode
Flag Suspicious Baggage/Passengers: Allows security personnel to flag suspicious baggage or passengers found during checking.
Airline Mode
Flight-Specific Views: Provides airlines with customized views showing baggage, passengers, and other details specific to their flights.
Check-in Lane and Belts: Allows airlines to access information about check-in lanes and baggage belts relevant to their flights.
Fleet Management: Provides real-time updates on flight status, changes, and delays to keep passengers informed.
Staff and Inventory Management: Enables airlines to manage ground forces, equipment, inventory, and vehicles related to flight operations. Addition or removal of personnel/vehicles is supported.
Overweight Baggage Check: Allows airlines to check for overweight baggage and calculate additional fees, if required.
Passenger/Bags Screening Flagging: Notifies airlines if a passenger or bag is flagged by security.
Privilege Assignment for Passengers: Allows airlines to increase passenger privileges, such as upgrades to business or first class.
Passenger Mode
Flight Information: Displays flight details for a given passenger.
Restaurant and Shop Information: Provides information about available restaurants, lounges, and shops within the airport premises.
Placing Orders: Allows passengers to place orders at airport shops.
Lost and Found Information: Enables passengers to register lost luggage on the portal, notifying respective airlines.
Dependencies

Java
Swing GUI Library
mysql-connector
rs2xml
Flatlaf
MySQL database
Database Initialization

To initialize the database for the Airport Management System, follow these steps:

Create a new MySQL database.
Import the initial database schema using the provided init_database.sql and init_procedures.sql files located in the database folder.
Example using the MySQL command-line tool:
php
Copy code
mysql -u <username> -p <database_name> init_database.sql
mysql -u <username> -p <database_name> init_procedures.sql
Replace <username> with your MySQL username and <database_name> with the name of the database you created.
How to Run

Clone the repository:
bash
Copy code
git clone https://github.com/siddharthcs187/AirportManagementSystem.git
Set up the MySQL database by following the steps mentioned in the "Database Initialization" section.
Open the project in an Integrated Development Environment (IDE) such as Eclipse or IntelliJ.
Build and compile the project, then run it.
