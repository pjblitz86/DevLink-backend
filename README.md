# 📌 DevLink Backend
> **A Java Spring Boot Backend for the DevLink Social Network Clone**  
> Must be run with frontend part which can be found here [DevLink Frontend](https://github.com/pjblitz86/DevLink-frontend)

---

## 📖 Overview
The **DevLink Backend** is a RESTful API developed using **Java Spring Boot** and **JPA**, with **MySQL** as the database. This backend powers the DevLink social networking platform, allowing developers to connect, share ideas, and find jobs.

The backend manages user authentication, profiles, posts, comments, jobs, and more. It is designed to be scalable and secure, following best practices in API development.

---

## 🚀 Features
- **User Authentication & Authorization** (JWT-based)
- **Profile Management** (CRUD operations for user profiles)
- **Posts & Comments** (Users can create, edit, delete posts & comments)
- **Jobs** (Create and find jobs)
- **Database Management** (Using JPA & MySQL)
- **Swagger API Documentation** (For easy API testing)

---

## 🏗 Tech Stack
- **Language:** Java 17+
- **Framework:** Spring Boot
- **Database:** MySQL
- **ORM:** JPA/Hibernate
- **Authentication:** JWT (JSON Web Token)
- **API Documentation:** Swagger
- **Build Tool:** Maven

---

## 🔧 Installation & Setup

### **Prerequisites**
Ensure you have the following installed on your machine:
- **Java 17+**
- **Maven**
- **MySQL**
- **Postman** (for API testing, optional)

### **Steps to Set Up Locally**
1. **Clone the repository**
   ```sh
   git clone https://github.com/pjblitz86/DevLink-backend.git
   cd DevLink-backend
   ```

2. **Configure Database**
   - Create a **MySQL database** named `devlink`
   - Update `application.properties` (in `src/main/resources`) with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/devlink
     spring.datasource.username=root
     spring.datasource.password=your_password
     ```

3. **Build & Run the Application**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access API**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 📌 API Endpoints
Here are some key API endpoints:

### **Authentication**
| Method | Endpoint           | Description          |
|--------|--------------------|----------------------|
| POST   | `/api/register`     | Register a new user  |
| POST   | `/api/login`        | Authenticate user    |

### **User**
| Method | Endpoint         | Description           |
|--------|-----------------|-----------------------|
| GET    | `/user/{id}`   | Get user by ID       |
| DELETE | `/users/{id}`   | Delete user account  |

### **Profiles**
| Method | Endpoint          | Description           |
|--------|------------------|-----------------------|
| Get   | `/api/profiles`     | Create a new post     |
| GET    | `/posts`        | Get all posts         |
| GET    | `/posts/{id}`   | Get post by ID        |
| DELETE | `/posts/{id}`   | Delete a post         |
| POST   | `/posts/{id}/comments` | Add a comment to a post |

### **Posts & Comments**
| Method | Endpoint          | Description           |
|--------|------------------|-----------------------|
| POST   | `/posts`        | Create a new post     |
| GET    | `/posts`        | Get all posts         |
| GET    | `/posts/{id}`   | Get post by ID        |
| DELETE | `/posts/{id}`   | Delete a post         |
| POST   | `/posts/{id}/comments` | Add a comment to a post |

### **Messaging System**
| Method | Endpoint         | Description           |
|--------|-----------------|-----------------------|
| POST   | `/messages/send` | Send a message       |
| GET    | `/messages/{id}` | Get message by ID    |

_For a full list of endpoints, refer to the Swagger documentation._


## 📝 License
This project is licensed under the MIT License.
