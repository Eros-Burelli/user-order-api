# User Order API

User Order API is a RESTful backend application built with Spring Boot for managing users, products, and orders.  
It features JWT-based authentication, role-based authorization (USER / ADMIN), and a layered architecture following real-world backend best practices.

This project was developed as a personal portfolio project to demonstrate backend development skills using Java and Spring Boot.

---

## Tech Stack

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- Maven
- MySQL
- JUnit 5
- Mockito
- MockMvc

---

## Main Features

- User registration and authentication with JWT
- Role-based access control (USER / ADMIN)
- Product management (CRUD operations restricted to ADMIN)
- Public product listing
- Order creation by authenticated users
- Orders composed of multiple products (Order & OrderItems)
- Automatic order total calculation
- Order status management (PENDING, CONFIRMED, SHIPPED)
- Users can view only their own orders
- Admins can view and manage all orders

---

## Security

- JWT authentication
- Role-based authorization using Spring Security
- Endpoint protection via `@PreAuthorize`
- Public registration endpoint assigns USER role by default
- Admin-only endpoints for product and order management

---

## Testing

- Unit tests for service layer using JUnit 5 and Mockito
- Controller tests using MockMvc
- Services mocked in controller tests
- Security filters disabled in controller tests (best practice)

---

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/Eros-Burelli/user-order-api.git

### 2. Configure environment variables
JWT_SECRET=your-secure-secret
JWT_EXPIRATION=3600000

### 3. Run the application
mvn spring-boot:run

## Project Structure

```text
src
 ├─ main
 │   └─ java
 │       └─ com
 │           └─ eros
 │               └─ userorderapi
 │                   ├─ config
 │                   ├─ controller
 │                   ├─ dto
 │                   │   ├─ request
 │                   │   └─ response
 │                   ├─ enums
 │                   ├─ exception
 │                   ├─ model
 │                   ├─ repository
 │                   ├─ security
 │                   └─ service
 └─ test
     └─ java
         └─ com
             └─ eros
                 └─ userorderapi
                     ├─ config
                     ├─ controller
                     └─ service
```

### Design notes
	- DTOs decouple API contracts from persistence entities
	- Business logic centralized in the service layer
	- Clean separation of concerns (Controller / Service / Repository)
	- JWT authentication implemented via a custom security filter
	- Centralized exception handling with custom error responses
	- Validation annotations ensure request correctness before reaching the service layer
	- All endpoints follow REST conventions with meaningful HTTP status codes

### Possible future improvements
	- Shopping cart support
	- Payment integration
	- Pagination and sorting for products and orders
	- Docker support
	- CI/CD with GitHub Actions

Author:
Eros Burelli  
Backend Developer – Java / Spring Boot  

This project was developed as a personal portfolio project.
