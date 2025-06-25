import java.sql.*;
import java.util.Scanner;

public class TeacherFunctions {

    static Scanner scanner = new Scanner(System.in);

    public static void teacherMenu() {
        while (true) {
            System.out.println("\n===== Teacher Menu =====");
            System.out.println("1. Add Marks");
            System.out.println("2. View Marks");
            System.out.println("3. Generate Report");
            System.out.println("4. Mark Attendance");      
            System.out.println("5. View Attendance");
            System.out.println("6. Register Student with Login");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) 
            {
                case 1:
                    addMarks();
                    break;
                case 2:
                    viewMarks();
                    break;
                case 3:
                    generateReport(); 
                    break;
                case 4: 
                    markAttendance(); 
                    break;
                case 5: 
                    viewAttendance(); 
                    break;
                case 6: 
                    registerStudentWithLogin(); 
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void addMarks() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter subject name: ");
            String subjectName = scanner.nextLine();

            System.out.print("Enter marks obtained: ");
            int marks = scanner.nextInt();
            scanner.nextLine();

            String subjectQuery = "SELECT subject_id FROM subjects WHERE subject_name = ?";
            PreparedStatement subjectStmt = conn.prepareStatement(subjectQuery);
            subjectStmt.setString(1, subjectName);
            ResultSet subjectRs = subjectStmt.executeQuery();

            int subjectId;
            if (subjectRs.next()) {
                subjectId = subjectRs.getInt("subject_id");
            } else {
        
                String insertSubject = "INSERT INTO subjects (subject_name) VALUES (?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSubject, Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, subjectName);
                insertStmt.executeUpdate();
                ResultSet keys = insertStmt.getGeneratedKeys();
                keys.next();
                subjectId = keys.getInt(1);
            }

            String insertMarks = "INSERT INTO marks (student_id, subject_id, marks_obtained) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertMarks);
            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, marks);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Marks added successfully!");
            } else {
                System.out.println(" Failed to add marks.");
            }

        } catch (SQLException e) {
            System.out.println(" Database error while adding marks.");
            e.printStackTrace();
        }
    }

    public static void viewMarks() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.student_id, s.name, sub.subject_name, m.marks_obtained " +
                           "FROM marks m " +
                           "JOIN students s ON m.student_id = s.student_id " +
                           "JOIN subjects sub ON m.subject_id = sub.subject_id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\n--- Student Marks ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("student_id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Subject: " + rs.getString("subject_name") +
                                   ", Marks: " + rs.getInt("marks_obtained"));
            }

        } catch (SQLException e) {
            System.out.println(" Error retrieving marks.");
            e.printStackTrace();
        }
    }
    public static void generateReport() {
    try (Connection conn = DBConnection.getConnection()) {
        System.out.print("Enter student ID to generate report: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String studentQuery = "SELECT name, class, section FROM students WHERE student_id = ?";
        PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
        studentStmt.setInt(1, studentId);
        ResultSet studentRs = studentStmt.executeQuery();

        if (!studentRs.next()) {
            System.out.println(" Student not found.");
            return;
        }

        String name = studentRs.getString("name");
        String className = studentRs.getString("class");
        String section = studentRs.getString("section");

        System.out.println("\n===== Report Card =====");
        System.out.println("Name: " + name);
        System.out.println("Class: " + className + ", Section: " + section);
        System.out.println("-----------------------------");

        String marksQuery = "SELECT sub.subject_name, m.marks_obtained FROM marks m " +
                            "JOIN subjects sub ON m.subject_id = sub.subject_id " +
                            "WHERE m.student_id = ?";
        PreparedStatement marksStmt = conn.prepareStatement(marksQuery);
        marksStmt.setInt(1, studentId);
        ResultSet marksRs = marksStmt.executeQuery();

        int total = 0, count = 0;
        while (marksRs.next()) {
            String subject = marksRs.getString("subject_name");
            int marks = marksRs.getInt("marks_obtained");
            System.out.printf("%-15s %5s %3d | %s\n", subject, "Marks:", marks, getGrade(marks));
            total += marks;
            count++;
        }

        if (count == 0) {
            System.out.println("No marks available.");
            return;
        }

        double average = (double) total / count;
        System.out.println("-----------------------------");
        System.out.println("Total: " + total + ", Average: " + average);
        System.out.println("Overall Grade: " + getGrade((int) average));

    } catch (SQLException e) {
        System.out.println(" Error generating report.");
        e.printStackTrace();
    }
}

private static String getGrade(int marks) {
    if (marks >= 90) return "A+";
    else if (marks >= 80) return "A";
    else if (marks >= 70) return "B";
    else if (marks >= 60) return "C";
    else if (marks >= 50) return "D";
    else return "F";
}
public static void markAttendance() {
    try (Connection conn = DBConnection.getConnection()) {
        System.out.print("Enter student ID: ");
        int studentId;
        try 
        {
            studentId = Integer.parseInt(scanner.nextLine().trim());
        } 
        catch (NumberFormatException e) 
        {
            System.out.println(" Invalid input. Please enter a number.");
            return;
        }       


        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter status (Present/Absent): ");
        String status = scanner.nextLine();

        String query = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, studentId);
        stmt.setString(2, date);
        stmt.setString(3, status);

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println(" Attendance marked successfully!");
        } else {
            System.out.println(" Failed to mark attendance.");
        }

    } catch (SQLException e) {
        System.out.println(" Error while marking attendance.");
        e.printStackTrace();
    }
}
public static void viewAttendance() {
    try (Connection conn = DBConnection.getConnection()) {
        System.out.print("Enter student ID to view attendance: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String query = "SELECT date, status FROM attendance WHERE student_id = ? ORDER BY date ASC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, studentId);
        ResultSet rs = stmt.executeQuery();

        System.out.println("\n===== Attendance Report =====");
        while (rs.next()) {
            String date = rs.getString("date");
            String status = rs.getString("status");
            System.out.printf("%-12s : %s\n", date, status);
        }

    } catch (SQLException e) {
        System.out.println(" Error retrieving attendance.");
        e.printStackTrace();
    }
}
public static void registerStudentWithLogin() {
    try (Connection conn = DBConnection.getConnection()) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter class: ");
        String className = scanner.nextLine();

        System.out.print("Enter section: ");
        String section = scanner.nextLine();

        System.out.print("Create a username for student login: ");
        String username = scanner.nextLine();

        System.out.print("Create a password for student login: ");
        String password = scanner.nextLine();

        String insertUser = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
        PreparedStatement userStmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
        userStmt.setString(1, username);
        userStmt.setString(2, password);
        userStmt.executeUpdate();

        ResultSet generatedKeys = userStmt.getGeneratedKeys();
        int userId = -1;
        if (generatedKeys.next()) {
            userId = generatedKeys.getInt(1);
        }

        String insertStudent = "INSERT INTO students (name, class, section, user_id) VALUES (?, ?, ?, ?)";
        PreparedStatement studentStmt = conn.prepareStatement(insertStudent);
        studentStmt.setString(1, name);
        studentStmt.setString(2, className);
        studentStmt.setString(3, section);
        studentStmt.setInt(4, userId);

        int rows = studentStmt.executeUpdate();
        if (rows > 0) {
            System.out.println(" Student with login registered successfully!");
        } else {
            System.out.println(" Failed to add student.");
        }

    } catch (SQLException e) {
        System.out.println(" Error during student registration.");
        e.printStackTrace();
    }
}


}
