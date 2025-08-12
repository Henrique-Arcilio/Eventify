# Challenge 03 - Compass UOL

## Overview

This project was developed as part of a challenge proposed by **Compass UOL**.  
The goal was to create two microservices — **Ticket Manager** and **Event Manager** — that communicate with each other to form a system for creating events and selling tickets. Users can manage their purchased tickets within the system.

---
## Technologies & Tools
- **Java:** 17 (LTS)
- **Spring Boot:** 3.5.4 (LTS)
- **Database:** MongoDB Atlas
  - Databases: `db-event` and `db-ticket`
- **HTTP Client:** OpenFeign
- **Deployment:** AWS EC2 

---
### API DOCUMENTATION
**You can acess the `ms-event-manager` swagger documentation at http://3.149.180.64:8081/swagger-ui/index.html#/**

**You can acess the `ms-ticket-manager` swagger documentation at http://3.139.26.61:8080/swagger-ui/index.html#/**


- Only the **Ticket Manager** microservice implements JWT authentication.
- **Event Manager** currently allows access to all endpoints without authentication.
----
## Microservices Description

### **Event Manager**
- Responsible for managing events.
- Communicates with **Ticket Manager** to ensure ticket information is updated when events change.
- Verifies if there are tickets related before allowing an event to be canceled.
- Consumes the **ViaCEP API** ([Webservice ViaCEP](https://viacep.com.br)) to retrieve address information based on the postal code (CEP) provided during event creation.

### **Ticket Manager**
- Manages two main entities:
  1. **Tickets for Sale** – typically managed by users with `ROLE_ADMIN` (this role must be created directly in the database).
  2. **Customer Tickets** – tickets purchased by customers.
- All users created through this service are assigned the `ROLE_CUSTOMER` role, except admins added directly in the database.
- Only `ROLE_ADMIN` can modify the "Tickets for Sale" data.
- Implements **JWT authentication** (refresh token endpoint exists but is not functional in this version).
- Consumes the **Event Manager** microservice to retrieve event details for ticket creation (location, date, name, and address).
## Database Overview

This project consists of two main microservices, each with its own database.
(See above for details on the database structure to help you set up the database environment.)


---

### 1. **Event Manager** Database

The Event Manager database contains the main collection called `events`, which stores all the events created in the system.

- **events**: collection storing event details such as event name, date, location, and other relevant information.

![MongoDB Compass - db-event events collection](./images/db-event.png)

---

### 2. **Ticket Manager** Database

The Ticket Manager database consists of three main collections:

- `customerTickets` (already purchased tickets): stores tickets that customers have already bought.

- `tickets` (tickets for sale): contains tickets available for purchase linked to events.

- `users`: stores system users. There are two user roles:
  - **ROLE_ADMIN**: administrators who manage tickets for sale.
  - **ROLE_CUSTOMER**: customers who purchase tickets and manage their own tickets.



![MongoDB Compass - db-ticket users collection](./images/db-ticket.png)

## Creating an Admin User Manually

To create an admin user directly in your database, **it is highly recommended to use the example below to avoid authentication issues**.

The password field is encrypted using **bcrypt** with the same configuration used by this application. The raw password for the bellow example is: `admin`

### Example admin user insertion for MongoDB
> **Important:**  
> Using third-party bcrypt generators or other encryption methods **may produce hashes incompatible with the application**, causing login failures.  
> To ensure your admin user works properly, copy and insert the JSON below into your MongoDB `db-ticket` database, `users` collection.
```js
db.users.insertOne({
  "username": "admin@test",
  "password": "$2a$10$p2of5mko0/cqxFOt/kSh7.6wQWS8Xho13Qg7lTBymOlZ3qsimGjfK",
  "fullname": "Main Administrator",
  "cpf": "000.000.000-00",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "enabled": true,
  "role": "ROLE_ADMIN",
  "_class": "com.arcilio.henrique.ms_ticket_manager.domain.model.User"
});
```
>**Optional:**
>If you really want to generate your own password hash, you can place the following method inside a simple Java class (for example, a utility class or a main class) just to generate the hash:

```java
public static void generatePassword() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String rawPassword = "your-password";
    String encodedPassword = encoder.encode(rawPassword);
    System.out.println("BCrypt password hash: " + encodedPassword);
}
```

## Environment Variables
### **Event Manager**
```yaml
spring:
  application:
    name: ms-event-manager
  data:
    mongodb:
      uri: ${MONGO_URL}

  cloud:
    openfeign:
      okhttp:
        enabled: true

feign:
  data-resource:
    name: via-cep
    url: ${VIA_CEP_URL}
  ticket-manager:
    name: ms-ticket-manager
    url: ${TICKET_MANAGER_URL}

server:
  port: 8081

MONGO_URL – MongoDB connection string for Event Manager

VIA_CEP_URL – Base URL for the ViaCEP API

TICKET_MANAGER_URL – Base URL of the Ticket Manager microservice

Example -> http://<TicketsHost>:8080/api/v1 (don't put 'tickets' in the end)
```

### **Ticket Manager**
```yaml
spring:
  application:
    name: ms-ticket-manager
  data:
    mongodb:
      uri: ${MONGO_URL}

security:
  jwt:
    token:
      secret-key: ${SECRET_KEY}
      expire-length: ${JWT_EXPIRATION_LENGTH}

feign:
  event-manager:
    name: ms-event-manager
    url: ${EVENT_MANAGER_URL}

springdoc:
  paths-to-match:
    - /api/v1/**
    - /auth/**

MONGO_URL – MongoDB connection string for Ticket Manager

SECRET_KEY – Secret key for JWT token generation

JWT_EXPIRATION_LENGTH – Token expiration time in milliseconds

EVENT_MANAGER_URL – Base URL of the Event Manager microservice

Example -> http://<EventsHost>:8081/api/v1/events

```

