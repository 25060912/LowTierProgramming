package app;
import java.util.*;
import API.API;
import logic.loginDatabase.*;
import logic.welcomeAndSummary.*;
import logic.welcomelogic.*;
import UI.*;


public class mainApp {
    public static void main(String[] args) {
        API api = new API();
        Scanner sc = new Scanner(System.in);

        /*LoginPage loginPage = new LoginPage();
        String username = loginPage.run(sc);

        if (username == null) return;

        WelcomeLogicMain welcomeLogic = new WelcomeLogicMain(username);
        welcomeLogic.run(sc);*/
        
        System.out.print("Enter your location: ");
        String loc = sc.nextLine();

        API.openWeatherResponse response = api.getWeatherByName(loc);

        System.out.println("Temperature: "+response.main.temp+"C");
        System.out.println("Feels like: "+response.main.feels_like+"C");
        System.out.println("Humidity: "+response.main.humidity+"%");
        System.out.println("Condition: "+response.weather[0].description+response.weather[0].icon);
        sc.close();
    }
}
