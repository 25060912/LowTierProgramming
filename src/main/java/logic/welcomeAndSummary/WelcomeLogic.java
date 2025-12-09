package welcomelogic;

import java.util.Scanner;

public class WelcomeLogic {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // --- Ask user for their name ---
        System.out.print("Enter your name: ");
        String userName = input.nextLine();

        // --- Greeting Logic Example ---
        String greeting = GreetingLogic.getGreeting(userName);
        System.out.println(greeting);

        // --- Journal Manager Example ---
        JournalManager journal = new JournalManager();
        journal.createOrUpdateJournal("First Entry", "Today I started my smart journal!");
        System.out.println("Viewing Journal: " + journal.viewJournal("First Entry"));

        // --- Mood Tracker Example ---
        MoodTracker mood = new MoodTracker();
        mood.addMood("Happy");
        mood.addMood("Tired");

        System.out.println("Weekly Mood Summary: " + mood.getWeeklySummary());

        input.close();
    }
}
