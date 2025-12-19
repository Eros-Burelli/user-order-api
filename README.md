# User Order API

User Order API is a RESTful backend application built with Spring Boot for managing users, products, and orders.
It includes JWT-based authentication, role-based authorization (USER / ADMIN), and automated testing.

This project was developed as a personal portfolio project to demonstrate real-world backend development practices using Java and Spring.

Tech stack:
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- Maven
- H2 / PostgreSQL (configurable)
- JUnit 5
- Mockito
- MockMvc

Main features:
- User registration and login with JWT
- Role management (USER by default, ADMIN for management operations)
- Product management (CRUD operations restricted to ADMIN)
- Public product listing
- Order creation by authenticated users
- Orders composed of multiple products (Order + OrderItems)
- Automatic order total calculation
- Order status management (CREATED, CONFIRMED, SHIPPED)
- Users can view only their own orders
- Admins can view and manage all orders

Security:
- JWT authentication
- Role-based authorization using Spring Security
- Endpoint protection via @PreAuthorize
- Public registration endpoint assigns USER role by default
- Admin-only endpoints for product and order management

Testing:
- Unit tests for service layer using JUnit 5 and Mockito
- Controller tests using MockMvc
- Services mocked in controller tests
- Security filters disabled in controller tests (best practice)

How to run the application:
1. Clone the repository:
   git clone https://github.com/Eros-Burelli/user-order-api.git

2. Configure environment variables:
   JWT_SECRET=your-secure-secret
   JWT_EXPIRATION=3600000

3. Run the application:
   mvn spring-boot:run

## Project Structure

```text
src
 ├─ main
 │   ├─ java
 │   │   └─ com
 │   │       └─ eros
 │   │           └─ userorderapi
 │   │               ├─ controller
 │   │               ├─ service
 │   │               ├─ repository
 │   │               ├─ model
 │   │               ├─ dto
 │   │               └─ security
 └─ test
     └─ java
         └─ com
             └─ eros
                 └─ userorderapi
                     ├─ controller
                     └─ service
```

Design notes:
- DTOs are used to decouple API contracts from persistence entities
- Business logic is centralized in the service layer
- Clean separation of concerns (Controller / Service / Repository)
- JWT authentication implemented via a custom security filter
- Tests written following real-world Spring Boot best practices

Possible future improvements:
- Shopping cart support
- Payment integration
- Advanced pagination and sorting
- Docker support
- CI/CD with GitHub Actions

Author:
Eros Burelli  
Backend Developer – Java / Spring Boot  

This project was developed as a personal portfolio project.
