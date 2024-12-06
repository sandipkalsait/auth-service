
# Auth-Service

## Overview
The **Auth-Service** is a Java Spring Boot application that implements authentication and authorization using Spring Security and JWT. It leverages a PostgreSQL database to manage user credentials and roles securely. This service provides a robust foundation for managing user authentication in modern microservice architectures.

## Features
- **JWT Authentication**: Secure token-based authentication.
- **Spring Security**: Robust security framework for authentication and authorization.
- **PostgreSQL Integration**: Efficient and reliable database for storing user data.
- **User Management**: APIs for registering, logging in, and managing users.
- **Role-Based Access Control (RBAC)**: Assign roles to users and restrict access to APIs based on roles.

## Prerequisites
- **Java 21**: Ensure Java Development Kit (JDK) 21 is installed.
- **Spring Boot 3.4.0**: Framework for building the application.
- **PostgreSQL**: Ensure PostgreSQL is installed and running.
- **Maven**: For managing dependencies and building the project.

## Getting Started

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/sandipkalsait/auth-service.git
   ```

2. Navigate to the project directory:
   ```bash
   cd auth-service
   ```

3. Configure the PostgreSQL database in the `application.yml` or `application.properties` file:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/authdb
       username: your-username
       password: your-password
     jpa:
       hibernate:
         ddl-auto: update
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
   ```

4. Build the project using Maven:
   ```bash
   mvn clean install
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

6. Access the application:
   - Base URL: `http://localhost:8080`

### API Endpoints

#### Public Endpoints
- **POST** `/api/auth/register` - Register a new user.
- **POST** `/api/auth/login` - Authenticate and retrieve a JWT token.

#### Protected Endpoints (Require JWT Token)
- **GET** `/api/users/me` - Get the currently authenticated user's details.
- **GET** `/api/admin` - Example endpoint accessible only to admin users.

### JWT Configuration
Upon successful login, a JWT token is returned. Include this token in the `Authorization` header of subsequent requests:
```
Authorization: Bearer <token>
```

## Database Schema
### User Table
| Column       | Type        | Description           |
|--------------|-------------|-----------------------|
| id           | UUID        | Primary key           |
| username     | VARCHAR     | Unique user identifier|
| password     | VARCHAR     | Hashed password       |
| roles        | VARCHAR     | User roles (comma-separated)|

## Technologies Used
- **Java 21**: Latest features and improvements in Java.
- **Spring Boot 3.4.0**: Framework for building scalable and secure applications.
- **Spring Security**: Built-in security features for authentication and authorization.
- **JWT**: Stateless authentication mechanism.
- **PostgreSQL**: Reliable relational database for storing user data.
- **Maven**: Build and dependency management tool.

## Testing
Run unit and integration tests:
```bash
mvn test
```

## Deployment
The application can be packaged as a JAR file and deployed using:
```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

## Contributing
Contributions are welcome. To contribute:
1. Fork the repository.
2. Create a new branch for your feature:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of changes"
   ```
4. Push to your branch and submit a pull request.

## License
This project is licensed under the [Apache License](LICENSE).

## Contact
For questions or support, please contact [Sandip.Kalsait](mailto:kalsaitsandip91@gmail.com).
