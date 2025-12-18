package app;
import java.util.Scanner;
import API.API;

public class mainApp {
    public static void main(String[] args) {
        System.out.println(System.getenv("OPENWEATHER_API_KEY"));

        API api = new API();
        logic.ghazy g = new logic.ghazy();
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter location: ");
        String loc = sc.nextLine();
        String weather = api.weatherAPI(loc);
        System.out.println(weather);

        System.out.print("Enter journal: ");
        String journal = sc.nextLine();
        String sentiment = g.analyze(journal);
        System.out.println(sentiment);

        sc.close();
    }
}