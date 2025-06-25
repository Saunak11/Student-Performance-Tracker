import java.sql.*;
import java.util.Scanner;

public class LoginSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println(" Login successful! Logged in as: " + role);

                switch (role) 
                {
                    case "admin":
                        System.out.println("Welcome, Admin!");
                        AdminFunctions.adminMenu();
                        break;
                    case "teacher":
                        System.out.println("Welcome, Teacher!");
                        TeacherFunctions.teacherMenu();
                        break;
                    case "student":
                        System.out.println("Welcome, Student!");
                        StudentFunctions.studentMenu(username);
                        break;
                    default:
                        System.out.println(" Unknown role!");
                }
            } else {
                System.out.println(" Invalid username or password.");
            }

        } catch (SQLException e) {
            System.out.println(" Login failed due to a database error.");
            e.printStackTrace();
        }

        scanner.close();
    }
}
