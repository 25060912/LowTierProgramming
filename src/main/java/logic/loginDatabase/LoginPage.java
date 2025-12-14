package logic.loginDatabase;

import java.util.Scanner;

public class LoginPage {
    
    public void run() {
        UserAuthenticator auth = new UserAuthenticator();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== User Authentication System ===");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1: // Login
                    System.out.print("Enter email/username: ");
                    String loginId = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    
                    if (auth.authenticate(loginId, loginPassword)) {
                        
                        String username = auth.getUsername(loginId); //get username from database
                        System.out.println("Login successful!");
                        System.out.println("Welcome, " + username+ "! You can now access the main application.");
                        loggedIn = true;
                    } else {
                        System.out.println("Login failed! Invalid credentials.");
                    }
                    break;
                    
                case 2: // Register
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    
                    //password confrimation
                    System.out.print("Confirm password: ");
                    String confirmPassword = scanner.nextLine();
                    if (!password.equals(confirmPassword)) {
                    System.out.println("Passwords do not match! Please try again.");
                    break;  
                    }
                    auth.registerUser(username, email, password);
                    break;
                    
                case 3: // Exit
                    System.out.println("Exiting application...");
                    auth.close();
                    scanner.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
