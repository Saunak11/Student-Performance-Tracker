import java.sql.*;
import java.util.Scanner;

public class AdminFunctions {

    static Scanner scanner = new Scanner(System.in);

    public static void adminMenu() {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void addStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();
            System.out.print("Enter class: ");
            String className = scanner.nextLine();
            System.out.print("Enter section: ");
            String section = scanner.nextLine();

            String query = "INSERT INTO students (name, class, section) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, className);
            stmt.setString(3, section);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Student added successfully!");
            } else {
                System.out.println(" Failed to add student.");
            }

        } catch (SQLException e) {
            System.out.println(" Database error while adding student.");
            e.printStackTrace();
        }
    }

    public static void viewStudents() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("student_id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Class: " + rs.getString("class") +
                                   ", Section: " + rs.getString("section"));
            }

        } catch (SQLException e) {
            System.out.println(" Error retrieving students.");
            e.printStackTrace();
        }
    }
}

