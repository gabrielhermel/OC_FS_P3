# ChâTop REST API: Backend Setup Guide

Back-end REST API for the **ChâTop** rental application, built with **Spring Boot**.

---

## Overview

This API provides rental listing management with secure JWT-based authentication:

- **Authentication**: User registration, login, and retrieval of authenticated user info
- **User Management**: Retrieval of user information by ID
- **Rental Listings**: Creation, updating, and retrieval of rental properties (by ID or all)
- **Messaging**: Creation of messages linked to rental listings
- **Media Handling**: Uploading and serving of rental images

This guide explains how to install, configure, and run the project locally.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Run the Application](#run-the-application)
- [Testing](#testing)

---

## Project Structure

```
src/main/java/com/chatop/backend/
├── annotation/ .................. # Custom annotations for Swagger error responses
├── config/ ...................... # Configuration (security, Swagger, file serving, upload content detection)
├── controller/ .................. # REST controllers
├── dto/ ......................... # Request and response DTOs
├── exception/ ................... # Global exception handling and custom exceptions
├── model/ ....................... # JPA entities
├── repository/ .................. # Data access layer
├── security/ .................... # JWT utilities and filters
├── service/ ..................... # Business logic and file storage
└── ChatopBackendApplication.java  # Main application class
```

---

## Prerequisites

Before running the API, ensure the following are installed:

* **Java 17 JDK** (or a more recent version)
* **MySQL** (version 8 or higher recommended)
* **Git** (to clone the repository)

Maven does not need to be installed globally, since the project includes the Maven wrapper (`mvnw`).

**Installation guides:**

* [Java JDK Installation Guide](https://docs.oracle.com/en/java/javase/17/install/)
* [MySQL Installation Guide](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/)
* [Git Installation Guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

**Verify installations:**

Terminal (Linux or Mac) / Command Prompt or PowerShell (Windows):

```bash
java -version
mysql --version
git --version
```

---

## Installation

### Clone the repository

Open a terminal (Linux/Mac) or Command Prompt/PowerShell (Windows) and navigate to the directory where you want to store the project.

Then clone the repository:
```bash
git clone https://github.com/gabrielhermel/OC_FS_P3.git
```

### Navigate to the project root
```bash
cd OC_FS_P3
```

You are now in the project's root directory, which is where the Maven wrapper (`mvnw`) commands in the **Run the Application** section should be executed.

---

## Database Setup

### Start MySQL

**Linux:**


(In a separate terminal which may be closed afterwards)

```bash
sudo systemctl start mysql
```

**macOS:**

If installed via Homebrew:


(In a separate terminal which may be closed afterwards)

```bash
brew services start mysql
```

Or use: *System Preferences > MySQL*

**Windows:**

Start MySQL using the *Services manager* or *MySQL Workbench*.

### Create the Database and User

Run the following script in your MySQL client (for example, `mysql -u root -p`):

```sql
-- Run as a privileged account (root)

-- Create (or drop and recreate) database
DROP DATABASE IF EXISTS chatop;
CREATE DATABASE IF NOT EXISTS chatop
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Ensure MySQL DB 'admin' user exists and has privileges on this schema
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON chatop.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;

-- Use created schema
USE chatop;

-- Create tables (lowercase to match Hibernate naming)
CREATE TABLE `users` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `rentals` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` bigint NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `messages` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `rental_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE UNIQUE INDEX `users_index` ON `users` (`email`);

ALTER TABLE `rentals`  ADD FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);
ALTER TABLE `messages` ADD FOREIGN KEY (`user_id`)  REFERENCES `users` (`id`);
ALTER TABLE `messages` ADD FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`);
```

This creates:

* `users`, `rentals`, and `messages` tables
* relationships between users, rentals, and messages
* an `admin` user with password `password` (development only) with full privileges on the `chatop` schema

**Verify the setup:**

```bash
mysql -u admin -p chatop
```

Enter password: `password`

Then in the MySQL prompt, run:

```sql
SHOW TABLES;
```

You should see:

```
+------------------+
| Tables_in_chatop |
+------------------+
| messages         |
| rentals          |
| users            |
+------------------+
```

---

## Configuration

The main configuration file is `src/main/resources/application.properties`.
All critical parameters have default values, so the application will run without additional configuration if the database setup steps were followed.

### Default database connection

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatop
spring.datasource.username=admin
spring.datasource.password=password
```

### Optional environment variables

Environment variables can be used to override defaults (useful for different environments or security concerns).

| Variable               | Default         | Description                           |
|------------------------|-----------------|---------------------------------------|
| `DB_HOST`              | localhost       | MySQL host                            |
| `DB_PORT`              | 3306            | MySQL port                            |
| `DB_NAME`              | chatop          | Database name                         |
| `DB_USERNAME`          | admin           | Database user                         |
| `DB_PASSWORD`          | password        | Database password                     |
| `JWT_SECRET`           | default value   | Secret key for JWT (base64, 32 bytes) |
| `JWT_EXPIRATION`       | 86400000        | JWT token expiration (milliseconds)   |
| `SWAGGER_NOAUTH`       | true            | Allow Swagger UI without login        |
| `UPLOAD_DIR`           | ./rental_images | Local image storage directory         |
| `UPLOAD_URL`           | /rental_images  | Base URL path to serve images         |
| `UPLOAD_CACHE_SECONDS` | 3600            | Cache duration for images (seconds)   |
| `UPLOAD_MAX_SIZE`      | 5MB             | Maximum file upload size              |

**Notes:**

- A default JWT secret is provided in `application.properties` for development purposes.
- Default values (especially credentials) should be changed for production deployments.

---

## Run the Application

From the project root, run:

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

**Windows:**

```cmd
mvnw.cmd spring-boot:run
```

When the server starts successfully, at the end of the logs you should see:

```
... : Tomcat started on port 8080 ...
... : Started ChatopBackendApplication ...
```

To stop the application, press **Ctrl + C** in the terminal.

---

## Testing

### Using Swagger UI

Swagger UI is accessible without login (by default) to test all endpoints, including authenticated ones, at:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Using an API client

Alternatively, use a standalone client like **Postman** or **Bruno** to test the API.

**Note:** When using a client, refer to Swagger UI for endpoint URLs and request/response formats.

### Authentication flow

To test authenticated endpoints:

- Register a new user via `POST /api/auth/register`
- Login via `POST /api/auth/login` to receive a JWT token
- Use the token to authorize requests:
  - In Swagger UI: Click the **Authorize** button and paste the token
  - In API clients: Use `Bearer <token>` in the `Authorization` header

### Accessing uploaded images

Uploaded images are stored in the `rental_images/` directory under the project root and served as static resources.

Example URL:

```
http://localhost:8080/rental_images/rental_1_1731254789123.jpg
```

(The actual filename will include a rental ID and timestamp to ensure uniqueness.)
