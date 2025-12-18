package logic.welcomeAndSummary;

import logic.welcomelogic.WeeklySummaryLogic;
import logic.Journal.*;
import java.util.Scanner;
import API.WeatherAPI;

public class WelcomeLogicMain {
    private String username;

    public WelcomeLogicMain(String username) {
        this.username = username;
    }
    // --- Greeting Logic ---

    public void run(Scanner sc) {

        String greeting = GreetingLogic.getGreeting(username);
        System.out.println("\n" + greeting + "\n");

        WeatherAPI api = new WeatherAPI();
        api.run(sc);

        JournalPage journalPage = new JournalPage();

        System.out.println("Choose an option:\n1. Journal Page\n2. Weekly Summary");
        boolean running = true;

        while (running) {
            System.out.print("Enter choice: ");
            String input = sc.nextLine();
            int option = Integer.parseInt(input);
            switch (option) {
                case 1: {
                    running = false;
                    journalPage.run();
                    break;
                }
                case 2: {
                    MoodTracker mood = new MoodTracker();
                    mood.addMood("Happy");
                    mood.addMood("Tired");

                    System.out.println("Mood History: " + mood.getWeeklySummary() + "\n");

                    // --- Weekly Summary Page (Weather + Mood for 7 days) ---
                    WeeklySummaryLogic summary = new WeeklySummaryLogic();
                    summary.addDailyRecord("Sunny", "Happy");
                    summary.addDailyRecord("Rainy", "Tired");
                    summary.addDailyRecord("Cloudy", "Relaxed");
                    summary.addDailyRecord("Windy", "Stressed");
                    summary.addDailyRecord("Hot", "Energetic");
                    summary.addDailyRecord("Sunny", "Calm");
                    summary.addDailyRecord("Stormy", "Anxious");

                    System.out.println(summary.getWeeklySummary());
                }
                default: {
                    System.out.println("Invalid input");
                    break;
                }
            }
        }
    }
}
