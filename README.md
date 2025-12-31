# URL Shortener

A simple Java web-based URL shortener service built with Servlets, JSP, and MySQL. It runs locally on Tomcat.

## Features

- Generate random short codes (6–12 characters) for long URLs
- Support custom short codes (1–12 characters, letters, numbers, `-`, `.`)
- Redirect from short URL to original URL
- Delete existing short codes
- Basic validation (URL format, short code uniqueness, length)
- Error handling and user feedback

## Tech Stack

- Java Servlet / JSP
- MySQL database
- Apache Tomcat (recommended 9+)
- NetBeans project structure

## Prerequisites

- JDK 8 or higher
- Apache Tomcat
- MySQL 5.7 or higher
- NetBeans (optional, for development)

## Database Setup

1. Run the SQL script to create the database and table:

   ```sql
   CREATE DATABASE IF NOT EXISTS url_shortener CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE url_shortener;
   CREATE TABLE url_mapping (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       short_code VARCHAR(12) UNIQUE NOT NULL,
       long_url TEXT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```
2. Modify WEB-INF/database.properties to set your database config
