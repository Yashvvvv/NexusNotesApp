# Spring Boot MongoDB Backend Authentication System

## Overview
This repository contains a Spring Boot application built with Kotlin that provides a comprehensive authentication system using MongoDB. The application includes secure user registration and login, JWT token management, and a notes management system to demonstrate protected resources. It leverages Spring Security for authentication and authorization, Spring Data MongoDB for data access, and follows modern reactive programming principles.

## Technologies Used
- **Kotlin 1.9.25**: Modern JVM language with concise syntax and null safety
- **Spring Boot 3.4.4**: Framework for building production-ready applications
- **MongoDB**: NoSQL database for storing user, token, and note data
- **Spring Data MongoDB**: Data access layer for MongoDB
- **Spring Security**: Authentication and authorization framework
- **JWT (JSON Web Tokens)**: For stateless authentication
- **Gradle**: Build automation tool using Kotlin DSL
- **JUnit 5**: Testing framework
- **Java 21**: Latest LTS version of Java

## Project Structure
```
src/
├── main/
│   ├── kotlin/
│   │   └── com/
│   │       └── example/
│   │           └── spring_boot_crash_course/
│   │               ├── controller/
│   │               │   ├── AuthController.kt     # Authentication endpoints
│   │               │   └── NoteController.kt     # Note management endpoints
│   │               ├── database/
│   │               │   ├── model/
│   │               │   │   ├── Note.kt           # Note document model
│   │               │   │   ├── RefreshToken.kt   # Token document model
│   │               │   │   └── User.kt           # User document model
│   │               │   └── repository/
│   │               │       ├── NoteRepository.kt # Data access for notes
│   │               │       ├── TokenRepository.kt # Data access for tokens
│   │               │       └── UserRepository.kt # Data access for users
│   │               ├── security/
│   │               │   ├── AuthService.kt        # Authentication business logic
│   │               │   ├── HashEncoder.kt        # Password hashing utility
│   │               │   ├── JwtAuthFilter.kt      # JWT authentication filter
│   │               │   ├── JwtService.kt         # JWT token management
│   │               │   └── SecurityConfig.kt     # Security configuration
│   │               ├── GlobalValidationHandler.kt # Global exception handling
│   │               └── SpringBootCrashCourseApplication.kt # Main application
│   └── resources/
│       └── application.properties    # Configuration properties
└── test/
    └── kotlin/
        └── com/
            └── example/
                └── spring_boot_crash_course/
                    └── # Test classes
```

## Application Architecture

### System Components & Flow Diagram

```
┌─────────────┐      ┌────────────────┐      ┌───────────────┐      ┌─────────────┐
│             │      │                │      │               │      │             │
│  Client     │ ──── │  Controllers   │ ──── │  Services     │ ──── │ Repositories│
│  (Mobile/Web)│      │  (API Layer)   │      │ (Business    │      │ (Data       │
│             │ ──── │                │ ──── │  Logic)      │ ──── │  Access)    │
└─────────────┘      └────────────────┘      └───────────────┘      └─────────────┘
                                                                          │
                                                                          │
                                                                          ▼
                                                                    ┌─────────────┐
                                                                    │             │
                                                                    │  MongoDB    │
                                                                    │  Database   │
                                                                    │             │
                                                                    └─────────────┘
```

### Authentication Flow

```
┌─────────┐     ┌───────────────┐     ┌─────────────┐     ┌───────────────┐     ┌───────────┐
│         │     │               │     │             │     │               │     │           │
│ Client  │ ──► │AuthController │ ──► │ AuthService │ ──► │ UserRepository│ ──► │ MongoDB   │
│         │     │               │     │             │     │               │     │           │
└─────────┘     └───────────────┘     └─────────────┘     └───────────────┘     └───────────┘
      ▲                                     │
      │                                     ▼
      │                              ┌─────────────┐
      └──────────────────────────── │  JwtService  │
          Returns JWT token           └─────────────┘
```

### Protected Resource Access Flow

```
┌─────────┐     ┌─────────────┐     ┌──────────────┐     ┌───────────────┐    ┌──────────┐
│         │     │             │     │              │     │               │    │          │
│ Client  │ ──► │JwtAuthFilter│ ──► │SecurityConfig│ ──► │NoteController │──► │Repository│
│ with JWT│     │             │     │              │     │               │    │          │
└─────────┘     └─────────────┘     └──────────────┘     └───────────────┘    └──────────┘
                       │                                                            │
                       ▼                                                            ▼
                 ┌─────────────┐                                             ┌───────────┐
                 │             │                                             │           │
                 │ JwtService  │                                             │ MongoDB   │
                 │             │                                             │           │
                 └─────────────┘                                             └───────────┘
```

## API Endpoints

### Authentication Endpoints
- `POST /auth/register`: Register a new user with email and password
- `POST /auth/login`: Authenticate a user and receive JWT tokens
- `POST /auth/refresh`: Refresh an expired access token using refresh token

### Notes Management Endpoints
- `POST /notes`: Create a new note (protected)
- `GET /notes?ownerId={ownerId}`: Get all notes for a specific owner (protected)
- `DELETE /notes/{id}`: Delete a specific note by ID (protected)

## Data Models

### User
- `id`: Unique identifier (MongoDB ObjectId)
- `email`: User's email address (unique)
- `hashedPassword`: Securely hashed password

### RefreshToken
- `id`: Unique identifier (MongoDB ObjectId)
- `token`: The refresh token string
- `userId`: Reference to the user
- `expiresAt`: Expiration timestamp

### Note
- `id`: Unique identifier (MongoDB ObjectId)
- `title`: Note title
- `content`: Note content
- `color`: Color code for the note (stored as Long)
- `createdAt`: Timestamp when the note was created
- `ownerId`: Reference to the owner of the note

## Security Implementation
The application implements a comprehensive security solution:
- Password hashing using BCrypt
- JWT-based authentication with access and refresh tokens
- Stateless authentication with token validation
- Protected endpoints requiring authentication
- Global exception handling for validation errors

## Data Flow

1. **User Registration**:
   - Client sends registration data (email/password)
   - Server validates input
   - Password is hashed
   - User is stored in MongoDB
   - JWT tokens (access + refresh) are generated and returned

2. **Authentication**:
   - User provides credentials
   - Server validates against stored hash
   - JWT tokens are generated and returned
   - Client stores tokens for subsequent requests

3. **Protected Resource Access**:
   - Client includes JWT in Authorization header
   - JwtAuthFilter intercepts and validates the token
   - If valid, the request proceeds to the controller
   - If invalid, a 401 Unauthorized response is returned

4. **Token Refresh**:
   - When access token expires, client uses refresh token
   - Server validates refresh token
   - New tokens are generated and returned

## Getting Started

### Prerequisites
- JDK 21 or later
- MongoDB running locally or accessible via network

### Configuration
Edit `application.properties` to configure:
- MongoDB connection
- JWT secret and expiration times
- Server port and other settings

### Running the Application
1. Clone the repository
2. Start MongoDB
3. Run the application:
```bash
./gradlew bootRun
```

### Building the Application
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

## License
This project is open source and available under the [MIT License](LICENSE).
