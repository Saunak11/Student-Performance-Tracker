import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Connected to the database!");
        } 
        else 
        {
            System.out.println("Connection failed!");
        }
    }
}
