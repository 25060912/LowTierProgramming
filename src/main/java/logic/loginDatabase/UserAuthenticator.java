package logic.loginDatabase;

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

    //get username by email or username 
    public String getUsername(String emailOrUsername) {
        String query = "SELECT username FROM users WHERE email = ? OR username = ?";
        
        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error retrieving username");
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void registerUser(String username, String email, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        //1.Validate email format
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Please enter a valid email address.");
            return;
        }
        
        //2. hash password
        if (hashedPassword == null) {
            System.out.println("Password hashing failed. Registration aborted.");
            return;
        }
        
        //3. Check if user already exists
        if (isUsernameExists(username)) {
            System.out.println("Username already taken. Please choose a different username.");
            return;
        }

        //4. Check if email already exists
        if (isEmailExists(email)) {
            System.out.println("Email already registered. Please use a different email.");
            return;
        }
        //5. Insert new user into database
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
    
    // Simple email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
         return email != null && email.matches(emailRegex);
    }

    //check if email exists in database
    private boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
    
        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
        
            pstmt.setString(1, email);
        
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }   
            }
        
        } catch (SQLException e) {
        System.err.println("Database error checking email existence");
        e.printStackTrace();
        }
    
        return false;
    }

    
    // Check if username exists in database
    private boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error checking username existence");
            e.printStackTrace();
        }
        
        return false;
    }
    public void close() {
        dbManager.closeConnection();
    }
}