# 📘 Student Performance Tracker – Backend System

A fully functional **console-based Java application** that helps institutions manage student records, academic performance, and attendance with a **role-based login system**.

---

## 👨‍💻 Developed by
**Saunak Saha**   
[GitHub Profile »](https://github.com/Saunak11)

---

## 🧰 Technologies Used

- **Java** (Core + JDBC)
- **MySQL** (Relational Database)
- Git & GitHub for version control

---

## 🧩 Features

- 🔐 **Secure role-based login system** (`Admin`, `Teacher`, `Student`)
- 👩‍🏫 **Admin Panel**  
  - Add/View student records
- 🧑‍🏫 **Teacher Panel**  
  - Add marks with grading
  - Record/view attendance
  - Generate detailed performance reports
  - Register new students with login credentials
- 🎓 **Student Panel**  
  - View personal grades, subjects, and attendance
- 📊 **Dynamic grade calculation logic**
- 🗃️ **Relational database design using MySQL**

---

## 🔧 Setup Instructions

> ✅ Prerequisites: Java JDK, MySQL, and the MySQL Connector JAR

1. Import `StudentTracker.sql` into MySQL.
2. Place the MySQL Connector JAR inside `/lib`.
3. Compile using:
   	javac -cp libmysql-connector-j-9.3.0.jar srcDBConnection.java srcLoginSystem.java srcAdminFunctions.java 	srcTeacherFunctions.java      srcStudentFunctions.java -d bin
4. Run the application: 
   java -cp libmysql-connector-j-9.3.0.jar;bin LoginSystem

**Clone the repository**:
   ```bash
   git clone https://github.com/Saunak11/Student-Performance-Tracker.git
   cd Student-Performance-Tracker
