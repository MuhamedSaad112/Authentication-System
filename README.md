# Spring Security Authentication System

## Overview
A comprehensive authentication and user management system built with Spring Boot 3.4.0 and Java 21. This project implements enterprise-level security patterns and modern architectural practices for secure user authentication and authorization.

## Database Design (Class Diagram)
![Authentication System](https://github.com/user-attachments/assets/500ef46f-d57d-4ff2-8339-68a2d521072c)


## Features
- *Advanced Authentication*
  - JWT-based token authentication
  - Refresh token mechanism
  - Secure password encryption with BCrypt

- *Role-Based Authorization*
  - Fine-grained access control
  - Dynamic permission management
  - User role hierarchies

- *User Management*
  - Email verification system
  - Password reset functionality
  - User profile management

- *Internationalization*
  - Multi-language support (English/Arabic)
  - Localized email templates
  - RTL/LTR support

- *Performance Optimization*
  - Redis caching implementation
  - Optimized database queries
  - Efficient resource utilization

- *Security Features*
  - XSS protection
  - CSRF protection
  - SQL injection prevention
  - Input validation

## Technical Stack
- *Core*
  - Java 21
  - Spring Boot 3.4.0
  - Spring Security

- *Database*
  - MySQL
  - Redis
  - Spring Data JPA

- *Security*
  - JWT (JJWT 0.11.5)
  - BCrypt password encoding
  - Spring Security

- *Development*
  - MapStruct 1.5.5.Final
  - Lombok
  - Maven

- *Documentation*
  - SpringDoc OpenAPI UI 2.7.0
  - Swagger UI

## Prerequisites
- Java 21
- Maven 3.x
- MySQL 8.x
- Redis Server

## Setup & Installation

1. *Clone the repository*
   bash
   git clone https://github.com/MuhamedSaad112/Authentication-System.git
   cd Authentication-System
   

2. *Configure MySQL*
   - Create a MySQL database
   - Update application.properties with your database credentials:
     properties
     spring.datasource.url=jdbc:mysql://localhost:3306/your_database
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     

3. *Configure Redis*
   - Ensure Redis server is running
   - Update Redis configuration in application.properties:
     properties
     spring.redis.host=localhost
     spring.redis.port=6379
     

4. *Configure Email Settings*
   - Update email configuration in application.properties:
     properties
     spring.mail.host=your.smtp.server
     spring.mail.port=587
     spring.mail.username=your_email
     spring.mail.password=your_password
     

5. *Build and Run*
   bash
   mvn clean install
   mvn spring-boot:run
   

## API Documentation
After running the application, access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Docs: http://localhost:8080/v3/api-docs

## API Endpoints

### Authentication & Registration

POST /api/v1/register
- Register a new user account
- Sends activation email
- Body: {
    username: string,
    password: string,
    email: string,
    firstName: string,
    lastName: string
}


### Account Management

GET /api/v1/activate?key={activationKey}
- Activate registered user account
- Query param: activation key received via email

GET /api/v1/authenticate
- Check if user is authenticated
- Returns username if authenticated

GET /api/v1/account
- Get current user details
- Returns user information with roles

PUT /api/v1/account
- Update user account information
- Body: {
    firstName: string,
    lastName: string,
    email: string,
    langKey: string,
    imageUrl: string
}


### Password Management

POST /api/v1/account/change-password
- Change current user's password
- Body: {
    currentPassword: string,
    newPassword: string
}

POST /api/v1/account/reset-password/init
- Initialize password reset
- Body: user's email address

POST /api/v1/account/reset-password/finish
- Complete password reset process
- Body: {
    key: string,
    newPassword: string
}


![WhatsApp Image 2024-12-27 at 23 09 39_3fd5f51d](https://github.com/user-attachments/assets/c66b2cca-18bc-40b6-9b0e-43a48c504593)


### Security Notes
- Password must be between 4 and 100 characters
- Email verification is required for account activation
- JWT authentication is required for protected endpoints
- Password reset links are sent via email
- All endpoints are protected against CSRF attacks


## Contributing
1. Fork the repository
2. Create your feature branch (git checkout -b feature/AmazingFeature)
3. Commit your changes (git commit -m 'Add some AmazingFeature')
4. Push to the branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

## Acknowledgments
- Spring Security Documentation
- JWT Authentication Best Practices
- Redis Caching Patterns

## 📞 Contact

📝 ***Feel free to contact me. I am always here ...***
[![Linkedin](https://img.shields.io/badge/LinkedIn-Mohamed%20Saad-blue?logo=Linkedin&logoColor=blue&labelColor=black)](https://www.linkedin.com/in/MuhamedSaad112/)
[![Mail](https://img.shields.io/badge/Gmail-m.saad1122003@gmail.com-blue?logo=Gmail&logoColor=blue&labelColor=black)](mailto:m.saad1122003@gmail.com)
<br>

## Author
Mohamed Saad - [GitHub Profile](https://github.com/MuhamedSaad112)


