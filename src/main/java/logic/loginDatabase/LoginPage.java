package logic.loginDatabase;

import java.util.*;

public class LoginPage {
    
    public String run(Scanner sc) {
        UserAuthenticator auth = new UserAuthenticator();
        
        System.out.println("=== User Authentication System ===");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            
            String input = sc.nextLine();
            int choice = Integer.parseInt(input);
            
            switch (choice) {
                case 1: // Login
                    System.out.print("Enter email/username: ");
                    String loginId = sc.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = sc.nextLine();
                    
                    if (auth.authenticate(loginId, loginPassword)) {
                        
                        String username = auth.getUsername(loginId); //get username from database
                        System.out.println("Login successful!");
                        System.out.println("Welcome, " + username+ "! You can now access the main application.");
                        loggedIn = true;
                        return loginId;
                    } else {
                        System.out.println("Login failed! Invalid credentials.");
                    }

                    break;
                    
                case 2: // Register
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    
                    //password confrimation
                    System.out.print("Confirm password: ");
                    String confirmPassword = sc.nextLine();
                    if (!password.equals(confirmPassword)) {
                    System.out.println("Passwords do not match! Please try again.");
                    break;  
                    }
                    auth.registerUser(username, email, password);
                    break;
                    
                case 3: // Exit
                    System.out.println("Exiting application...");
                    auth.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } return null;
    }
}
