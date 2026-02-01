# 🚀 User Registration & Login Servic

## 📌 Overview

This project implements a **User Registration and Login API** using Spring Boot, focusing on **robust exception handling, structured logging, and production-ready logging configuration** as required in **Module 4 – Assignment Set 2** and **Module 5 (Prod readiness)**.

The design follows clean architecture principles, centralized error handling, and safe logging practices suitable for real-world backend services.

## ✨ Features Implemented

### 👤 1. User Registration & Login

* Register a new user with unique username and email
* Login using username and password
* Response DTOs never expose passwords

---

### ⚠️ 2. Custom Business Exceptions

The following custom runtime exceptions are implemented to represent business rule violations:

* `UserAlreadyExistsException` – thrown when username or email already exists
* `UserNotFoundException` – thrown when user is not found or credentials are invalid

These exceptions are **not handled in controllers** and are delegated to the global exception handler.

---

### 🛡️ 3. Global Exception Handling

A **single global exception handler** is implemented using `@RestControllerAdvice` to handle:

* **Validation Errors** (`MethodArgumentNotValidException`)

  * Returns field-wise error messages
  * Logs validation failures without exposing request data

* **Business Exceptions**

  * Handles `UserAlreadyExistsException` and `UserNotFoundException`
  * Returns meaningful HTTP status codes (`409`, `404`)

This ensures consistent error responses and avoids duplicate try-catch blocks.

---

### 🧾 4. Logging Strategy

Logging is implemented using **SLF4J + Logback** with the following rules:

* Logging is done only in **service and exception layers**
* Controllers do not log and throw exceptions redundantly
* `INFO` level logs normal business flow
* `WARN` level logs handled business failures
* No passwords or sensitive data are logged
* Contextual information like `username` or `userId` is included where applicable

---

### 🛠️ 5. Logback Configuration

The `logback-spring.xml` file is configured with:

* Console appender for development visibility
* Rolling file appender for application logs
* Separate rolling file appender for `ERROR` level logs
* Package-based log levels (service, exception, framework)

Log files are written to a configurable `logs/` directory.

---

### ⚡ 6. Async Logging

Asynchronous logging is enabled using Logback’s `AsyncAppender`.

**Why async logging is used:**

* Prevents I/O operations from blocking request threads
* Improves application performance under load
* Ensures logging does not impact API response time

Even with a minimal setup, async logging reflects production-ready logging behavior.

---

## 🔐 How I Avoided Logging Sensitive Data

* Password fields are never logged at any level
* Request DTOs are not logged directly
* Logs include only safe identifiers like `username` and `userId`
* A dedicated `safeToString()` approach is used where entity logging is required

This ensures compliance with security best practices and logging guidelines.

---

## ✅ Common Rules Compliance

✔ No `printStackTrace()` used
✔ No `catch (Exception e)` unless rethrowing
✔ Controllers do not log and throw the same exception
✔ Logging adds context, not noise
✔ Async logging explanation provided

---

## Production Readiness

* Externalized logging configuration
* Async and file-based logging enabled
* Logs excluded from version control using `.gitignore`
* Clean separation of concerns (Controller / Service / Repository)
* 

## Conclusion

This project demonstrates a **production-quality user authentication service** 
proper exception handling, safe and structured logging, and performance-aware
configuration, fully aligned with Module 4 and Module 5 assignment requirements.
