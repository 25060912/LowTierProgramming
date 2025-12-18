package app;
import java.util.*;
import logic.loginDatabase.*;
import logic.welcomeAndSummary.*;
import logic.welcomelogic.*;
import UI.*;


public class mainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LoginPage loginPage = new LoginPage();
        String username = loginPage.run(sc);

        if (username == null) return;

        WelcomeLogicMain welcomeLogic = new WelcomeLogicMain(username);
        welcomeLogic.run(sc);

        sc.close();
    }
}
