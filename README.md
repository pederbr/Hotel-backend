# Hotel-backend

## Description

This is a backend for a hotel management system. It is built using Spring Boot and Java. It is a RESTful API that allows the user to perform CRUD operations on the database. The database is a MySQL database. The API is secured using JWT tokens. 

## Prerequisites

- Java 23
- docker

## Installation

```bash	

docker-compose up

./mvnw spring-boot:run


```

## Audit Log Endpoints
Base path: `/api/auditlog`
> Requires admin scope for all endpoints

### Get Audit Logs by Entity
```
GET /entity/{entityType}/{entityId}
```
Retrieves audit logs for a specific entity type and ID.

### Get Audit Logs by User
```
GET /user/{userId}
```
Retrieves audit logs for a specific user.

### Get Audit Logs by Date Range
```
GET /date/{start}/{end}
```
Retrieves audit logs within a specified date range.
- Dates should be in ISO format

## Booking Endpoints
Base path: `/api/bookings`

### List All Bookings
```
GET /
```
Retrieves all bookings (admin only).

### Get Booking by ID
```
GET /{id}
```
Retrieves a specific booking by ID (admin only).

### Get Bookings by Customer
```
GET /customer/{customerId}
```
Retrieves all bookings for a specific customer (admin only).

### Get Bookings by Room
```
GET /room/{roomId}
```
Retrieves all bookings for a specific room (admin only).

### Create Booking
```
POST /
Parameters:
- userId (int, required)
- roomId (int, required)
- start (string, required)
- end (string, required)
```
Creates a new booking.

### Update Booking
```
PUT /{id}
Parameters:
- newUserId (int, required)
- newRoomId (int, required)
- newStart (string, required)
- newEnd (string, required)
```
Updates an existing booking.

### Cancel Booking
```
DELETE /{id}
```
Cancels/deletes a booking.

## Customer Endpoints
Base path: `/api/customers`

### Create Customer
```
POST /
Parameters:
- name (string, required)
- email (string, required)
- phone (string, required)
```
Creates a new customer.

### Update Customer
```
PUT /{id}
Parameters:
- name (string, required)
- email (string, required)
- phone (string, required)
```
Updates an existing customer.

### Set Customer as Admin
```
PUT /{id}/setAdmin
```
Sets a customer as admin (requires admin scope).

### List All Customers
```
GET /
```
Retrieves all customers.

### Get Customer by ID
```
GET /{id}
```
Retrieves a specific customer by ID.

## Room Endpoints
Base path: `/api/rooms`

### List All Rooms
```
GET /
```
Retrieves all rooms (admin only).

### List Available Rooms
```
GET /available
```
Retrieves all available rooms.

### Get Room by ID
```
GET /{id}
```
Retrieves a specific room by ID (admin only).

### Get Available Room by ID
```
GET /available/id/{id}
```
Retrieves a specific available room by ID.

### Get Available Rooms by Type
```
GET /available/type/{type}
```
Retrieves available rooms of a specific type.

### Create Room
```
POST /
Parameters:
- roomNumber (string, required)
- type (string, required)
- status (string, required)
- pricePerNight (double, required)
```
Creates a new room.

## Authentication Notes
- Many endpoints require admin scope (`SCOPE_admin`)
- Admin scope is required for:
  - All audit log endpoints
  - Most booking list operations
  - Setting customer admin status
  - Several room management operations

## Response Formats
- All successful responses return either:
  - A single object of the requested type
  - An `Iterable` collection of objects
  - HTTP 204 (No Content) for deletions
- Failed requests may return:
  - 404 Not Found for missing resources
  - 403 Forbidden for unauthorized access
  - 400 Bad Request for invalid inputs