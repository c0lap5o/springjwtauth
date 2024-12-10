# 🎉 Spring Boot JWT Authentication API Documentation

Welcome to the world of **Spring Boot** and **JWT Authentication**! This documentation will guide you through the exciting endpoints of our API, where you can test user roles, authenticate users, and register new accounts. Let’s dive in!

## 🧪 TestController

The `TestController` is your playground for testing different user roles and access levels. It’s only active when you're in the 'dev' profile—perfect for development fun!

### 🚀 Endpoints

1. **Get Public Content**
   - **URL:** `/api/test/all`
   - **Method:** GET
   - **Description:** Fetches public content accessible to everyone. No secret handshake needed!
   - **Response:** A friendly string saying "Public Content."

2. **Get User Content**
   - **URL:** `/api/test/user`
   - **Method:** GET
   - **Description:** Retrieves user content for those with USER, MODERATOR, or ADMIN roles. 
   - **Security:** Requires JWT authentication (bring your token!).
   - **Response:** A delightful string saying "User Content."

3. **Get Moderator Content**
   - **URL:** `/api/test/mod`
   - **Method:** GET
   - **Description:** Only for our MODERATOR friends! Get exclusive moderator content.
   - **Security:** JWT authentication required.
   - **Response:** A cool string saying "Moderator Board."

4. **Get Admin Content**
   - **URL:** `/api/test/admin`
   - **Method:** GET
   - **Description:** For the elite ADMINs only! Access your special admin content.
   - **Security:** JWT authentication needed.
   - **Response:** An important string saying "Admin Board."

## 🔐 AuthController

The `AuthController` is where the magic happens for user authentication and registration. Get ready to sign in or sign up!

### 🌟 Endpoints

1. **Authenticate User**
   - **URL:** `/api/auth/signin`
   - **Method:** POST
   - **Description:** Authenticate your user credentials and get your shiny JWT token!
   - **Request Body:** `LoginRequest` (username, password)
   - **Response:** `JwtResponse` (token, id, username, roles) – your golden ticket!

2. **Register User**
   - **URL:** `/api/auth/signup`
   - **Method:** POST
   - **Description:** Sign up for a new user account and join the fun!
   - **Request Body:** `SignupRequest` (username, email, password, role)
   - **Response:** `MessageResponse` indicating if you’re successfully registered or if there’s an issue.

## 🔒 Security Configuration

We’ve got your back with Spring Security and JWT! Here’s what’s under the hood:
- `JwtUtils`: Your trusty sidekick for generating and validating JWT tokens.
- `UserDetailsImpl`: The superhero that implements UserDetails for custom user authentication.
- `AuthenticationManager`: The gatekeeper managing the authentication process.
- `PasswordEncoder`: Making sure your passwords are safe and sound.

## 🎭 Role-Based Access Control

Our application supports role-based access control with these cool roles:
- USER
- MODERATOR
- ADMIN

Roles are assigned during registration and enforced using `@PreAuthorize` annotations on controller methods. So pick your role wisely!

## 🌍 Cross-Origin Resource Sharing (CORS)

CORS is enabled for all origins with a max age of 3600 seconds—because sharing is caring!

## 📜 Swagger Documentation

We’ve made it easy to explore our API with Swagger annotations:
- Operation summaries and descriptions
- Request/response schemas
- Security requirements

Access them at localhost:8080/swagger-ui
## 📣 Logging

We use SLF4J for logging important events like sign-ins and registrations. Keep an eye on the logs to see what’s happening behind the scenes!

---

And there you have it! Enjoy exploring our Spring Boot JWT Authentication API. Happy coding! 🎈
