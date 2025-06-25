import java.sql.*;
import java.util.Scanner;

public class StudentFunctions {

    static Scanner scanner = new Scanner(System.in);

    public static void studentMenu(String username) {
        int studentId = getStudentIdFromUsername(username);
        if (studentId == -1) {
            System.out.println(" Student record not found.");
            return;
        }

        while (true) {
            System.out.println("\n===== Student Menu =====");
            System.out.println("1. View Marks & Grades");
            System.out.println("2. View Attendance");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewMarks(studentId);
                    break;
                case 2:
                    viewAttendance(studentId);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static int getStudentIdFromUsername(String username) {
        // Assumes student's username == their name in `students` table
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT student_id FROM students WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("student_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void viewMarks(int studentId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT sub.subject_name, m.marks_obtained " +
                           "FROM marks m " +
                           "JOIN subjects sub ON m.subject_id = sub.subject_id " +
                           "WHERE m.student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Your Marks & Grades ---");
            int total = 0, count = 0;
            while (rs.next()) {
                String subject = rs.getString("subject_name");
                int marks = rs.getInt("marks_obtained");
                System.out.printf("%-15s %5s %3d | Grade: %s\n", subject, "Marks:", marks, getGrade(marks));
                total += marks;
                count++;
            }

            if (count == 0) {
                System.out.println("No marks found.");
                return;
            }

            double avg = (double) total / count;
            System.out.println("------------------------------");
            System.out.printf("Total: %d, Average: %.2f, Overall Grade: %s\n", total, avg, getGrade((int) avg));

        } catch (SQLException e) {
            System.out.println(" Error fetching marks.");
            e.printStackTrace();
        }
    }

    private static void viewAttendance(int studentId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT date, status FROM attendance WHERE student_id = ? ORDER BY date ASC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Your Attendance ---");
            while (rs.next()) {
                String date = rs.getString("date");
                String status = rs.getString("status");
                System.out.printf("%-12s : %s\n", date, status);
            }

        } catch (SQLException e) {
            System.out.println(" Error fetching attendance.");
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
}
