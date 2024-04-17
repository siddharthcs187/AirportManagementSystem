# Airport Management System

Airport Management System is a Java application designed to manage an airport, flights, passengers, staff, and inventory. The application is built using Java, MySQL, and Swing.

## Features

### Admin View
- **Full System Overview with Search Functionality:** Provides a comprehensive dashboard/interface where administrators can access information about all baggage, flights, passengers, staff, and vehicles currently within the airport system.
- **Suspicious Baggage/Passenger Notification:** When security personnel flag baggage/passengers as suspicious, administrators and relevant airlines can view this information.
- **Shop Information:** Displays information about shops and their total revenue.
- **Adding Incoming Flights:** Enables administrators to add incoming flights, passengers, bags, and new airlines to the system. Administrators can assign gates and check-in lanes.

### Security View
- **Flag Suspicious Baggage:** Security personnel can flag suspicious baggage.
- **Flag Suspicious Passengers:** Security personnel can flag suspicious passengers.

### Airline View
- **Flight-Specific Views:** Airlines have customized views showing only baggage, passengers, and other details of their flights.
- **Check-in Lane and Belts:** Provides information about check-in lanes and baggage belts.
- **Fleet Management:** Real-time updates on flight status, changes, and delays.
- **Staff and Inventory Management:** Manage ground forces, equipment, inventory, and vehicles related to flight operations.
- **Overweight Baggage Check:** Check for overweight baggage and calculate additional fees.
- **Passenger/Bags Screening Flagging:** Airlines are notified if a passenger or bag is flagged by security.
- **Privilege Assignment for Passengers:** Increase privileges for passengers, e.g., upgrade to higher class.

### Passenger View
- **Flight Information:** Displays a passenger’s flight information.
- **Restaurant and Shop Information:** Information about available restaurants, lounges, and shops.
- **Placing Orders:** Passengers can place orders at airport shops.
- **Lost and Found Information:** Register lost luggage, notifying respective airlines.

## Dependencies
- Java
- Swing GUI Library
- mysql-connector
- rs2xml
- Flatlaf
- MySQL database

## Database Initialization
1. Create a new MySQL database.
2. Import initial database schema using `init_database.sql`, `init_procedures.sql` files.
   Example using MySQL command-line:
```
mysql -u <username> -p <database_name> init_database.sql
mysql -u <username> -p <database_name> init_procedures.sql
```

   Replace `<username>` with MySQL username and `<database_name>` with database name.

## How to Run
1. Clone the repository:
```
git clone https://github.com/siddharthcs187/AirportManagementSystem.git
```
2. Set up MySQL database as mentioned in "Database Initialization" section.
3. Open the project in an IDE like Eclipse or IntelliJ.
4. Build, compile, and run the project.
