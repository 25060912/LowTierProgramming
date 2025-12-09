import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthenticator {
    
    private DatabaseManager dbManager;
    
    public UserAuthenticator() {
        dbManager = new DatabaseManager();
    }
    
    public boolean authenticate(String emailOrUsername, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        if (hashedPassword == null) {
            return false;
        }
        
        String query = "SELECT password FROM users WHERE email = ? OR username = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    return hashedPassword.equals(storedHashedPassword);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error during authentication");
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void registerUser(String username, String email, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        if (hashedPassword == null) {
            System.out.println("Password hashing failed. Registration aborted.");
            return;
        }
        
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Registration failed.");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error during registration");
            e.printStackTrace();
        }
    }
    
    public void close() {
        dbManager.closeConnection();
    }
}