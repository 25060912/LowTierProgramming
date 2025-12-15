package logic.welcomeAndSummary;

import logic.welcomelogic.WeeklySummaryLogic;

public class WelcomeLogicMain {
    private String username;

    public WelcomeLogicMain(String username) {
        this.username = username;
    }

    public void run() {

        // --- Greeting Logic ---
        String greeting = GreetingLogic.getGreeting(username);
        System.out.println("\n" + greeting + "\n");

        // --- Journal Manager Example ---
        JournalManager journal = new JournalManager();
        journal.createOrUpdateJournal("First Entry", "Today I started my smart journal!");
        System.out.println("Viewing Journal: " + journal.viewJournal("First Entry") + "\n");

        // --- Mood Tracker Example ---
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
}
