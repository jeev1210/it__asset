# IT Company Asset Portal

**I built a full-stack IT Company Asset Portal from scratch using Spring Boot, PostgreSQL, Thymeleaf, and Spring Security. This system centralizes IT asset tracking, assignments, ticketing, maintenance, and employee feedback to enhance efficiency, accountability, and IT operations.**

---

## 📸 Screenshots

### 🏠 Homepage

![Homepage](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/HomeScreen.png)

### 👨‍💼 Admin

* [Admin Dashboard](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_dashboard.png)
* [Asset Management](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_Asset.png)
* [Assignment Management](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_Assignment.png)
* [Employee Management](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_Employees.png)
* [Maintenance Management](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_Maintenance.png)
* [Ticket Management](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_ticket.png)
* [Add Asset](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_Addasset.png)
* [Add Assignment](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_Addassignment.png)
* [Pending Users](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_PendingUsers.png)
* [Debug Users](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin-DebugUser.png)


### 👨‍💻 Employee

* [Employee Dashboard](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/EmployeeDashboard.png)
* [Assigned Assets](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Employee_Asset.png)
* [Create Ticket](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Employee_CreateTicket.png)
* [My Tickets](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Employee_Myticket.png)
* [Feedback](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Employee_Feedback.png)
* [Assignments](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Employee_Assignments.png)
* [Profile](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/EmployeeProfileEditandView.png)

### 🔐 Authentication

* [Login Page](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Login.png)
* [Register Page](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/registerpage.png)

### 🗄️ Database

* [PostgreSQL](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/PostgreSQL_Db.png)

---

## 🎯 Purpose

The purpose of the IT Asset Management System (ITAMS) is to provide organizations with a centralized, reliable, and efficient platform to manage their IT assets throughout their entire lifecycle. Modern companies struggle with tracking devices, ensuring accountability, handling support tickets, and maintaining assets manually — leading to delays, losses, and operational inefficiencies.

---

## 🚨 Problem Statement

Organizations often face these challenges when managing IT assets:

* **Assets are lost or misused:** Devices can be misplaced or improperly used due to lack of centralized tracking
* **Accountability issues:** Difficult to know which employee is assigned which asset
* **Delayed technical support:** Maintenance requests and technical issues take longer to resolve
* **Manual processes:** Tracking assets, managing maintenance, and coordinating with vendors manually is error-prone

**Solution:** ITAMS provides a unified platform to:

* Track the full lifecycle of assets from procurement to retirement
* Assign assets to employees with complete accountability and history
* Manage tickets, maintenance, and vendor tasks efficiently
* Collect employee feedback to improve IT services
* Streamline operations with automated, error-free workflows

---

## 🧠 Key Features

### 👨‍💼 Admin

* Dashboard: Overview of employees, assets, assignments, and tickets
* Employee Management (CRUD)
* Asset Management (CRUD)
* Assignment Management (CRUD)
* Ticket Management (CRUD)
* Maintenance Management (CRUD)
* Feedback Management (CRUD)
* Pending User Approval
* Debug Utilities

### 👨‍💻 Employee

* Dashboard & Asset Details
* Ticket Management (CRUD)
* Feedback Management (CRUD)
* Profile Management

---

## 🏗️ System Workflow

### Admin

1. Login → Admin Dashboard
2. Manage employees, assets, assignments, tickets, and feedback
3. Review pending user registrations → Approve/Reject
4. Assign assets to employees
5. Manage maintenance and notify vendors

### Employee

1. Register → Account pending
2. Login after admin approval
3. View assigned assets and details
4. Create tickets → Add comments → Upload attachments
5. Submit feedback
6. Return assets tracked by admin

---

## 🗂️ Project Structure

```text
IT Asset Tracking Portal (ITAMS)
├── src/
│   ├── main/
│   │   ├── java/com/example/itassettrackingportal/
│   │   │   ├── config/          # Security & data initialization
│   │   │   ├── controller/      # Admin, Employee, Asset, Assignment,Ticket, Feedback,
│   │   │   ├── model/           # Entities & enums
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── dto/             # DTOs
│   │   ├── resources/
│   │   │   ├── static/          
│   │   │   ├── templates/       # Thymeleaf templates
│   │   │   │   ├── admin/
│   │   │   │   ├── employee/
│   │   │   │   ├── fragments/
│   │   │   │   ├── tickets/
│   │   │   │   └── index.html, login.html, register.html
│   │   │   └── application.properties
```
* [Project Structure](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Project_Structure.png)

---

## ⚙️ Configuration

```properties
spring.application.name=it-asset-management
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/itams_db
spring.datasource.username=postgres
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.thymeleaf.cache=false
jwt.secret=yourSuperStrongRandomSecretKeyAtLeast32Chars
jwt.expirationMs=3600000
spring.main.allow-bean-definition-overriding=true
```

---

## 🛠️ Tech Stack

* Backend: Spring Boot 3.3.4, Java 17, Spring Security, Spring Data JPA, PostgreSQL, Lombok
* Frontend: Thymeleaf, Bootstrap 5.3.3, Font Awesome 6.6.0, Google Fonts (Inter)
* Build Tool: Maven
* Deployment: Embedded Tomcat (Port 8080)

---

## 🔐 Security

* Session-based authentication (JSESSIONID)
* BCrypt password encoding
* Role-based login redirects
* CSRF protection enabled
* Custom `UserDetailsServiceImpl`
* Pending user approval before login
* [User Approval Management](https://github.com/NithushanUthayarasa/IT-Company-Asset-Portal/blob/main/screenshots/Admin_PendingUsers.png)

---

## 🚀 Run the Project

1️⃣ Clone the repository:

```bash
git clone <your-repository-url>
```

2️⃣ Configure PostgreSQL:

```sql
CREATE DATABASE itams_db;
```

3️⃣ Update `application.properties` with your DB credentials

4️⃣ Run the project:

```bash
mvn spring-boot:run
```

### 🔑 Default Admin Login

* Email: [admin@system.com](mailto:admin@system.com)
* Password: ChangeMe123! (change after first login)

---

## 📌 Highlights

* Centralized asset tracking & assignment history
* Full CRUD for all core modules
* Asset lifecycle & maintenance management
* Vendor notifications
* Employee feedback management
* Clean & responsive UI
* Role-based dashboards with session security
* Pending user approval workflow

---

## 📄 License

This project is for **educational and learning purposes only**.

---

**Author:** NithushanUthayarasa
