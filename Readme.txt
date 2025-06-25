📘 Student Performance Tracker – Backend System
------------------------------------------------

👨‍💻 Developed by: Saunak Saha

🧰 Technologies Used:
- Java (Core + JDBC)
- MySQL (RDBMS)
- Console-based interaction

🧩 Features:
- Admin: Add/View students
- Teacher: Add/View marks, attendance, generate reports
- Student: View own marks, grades, attendance
- Grade calculation logic built-in
- Secure login with role-based access

🔧 Setup Instructions:
1. Import `StudentTracker.sql` into MySQL.
2. Place the MySQL Connector JAR inside `/lib`.
3. Compile using:
   	javac -cp libmysql-connector-j-9.3.0.jar srcDBConnection.java srcLoginSystem.java srcAdminFunctions.java 	srcTeacherFunctions.java srcStudentFunctions.java -d bin
4. Run the application:
   java -cp libmysql-connector-j-9.3.0.jar;bin LoginSystem

🧪 Sample Logins:
- Admin:     admin01 / adminpass
- Teacher:   teacher01 / teachpass
- Student:   student01 / studpass
  (Add more using the Register Student option as Teacher)

📂 Directory Structure:
  See project folder layout for files.

-----------------------------------------------
✅ Ready for demonstration / evaluation.
