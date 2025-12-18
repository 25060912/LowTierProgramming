package logic.welcomelogic;

import java.util.*;

public class WeeklySummaryLogic {

    // Holds the last 7 days of weather and mood
    private final List<DayRecord> records = new ArrayList<>();

    // Inner class representing daily data
    private static class DayRecord {
        String weather;
        String mood;

        DayRecord(String weather, String mood) {
            this.weather = weather;
            this.mood = mood;
        }
    }

    /**
     * Adds a weather + mood record for a day.
     * Automatically keeps only the last 7 entries.
     */
    public void addDailyRecord(String weather, String mood) {
        if (records.size() == 7) {
            records.remove(0); // remove oldest entry
        }
        records.add(new DayRecord(weather, mood));
    }

    /**
     * Returns a formatted summary of the past 7 days.
     */
    public String getWeeklySummary() {
        if (records.isEmpty()) {
            return "No data available for this week.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📊 Weekly Weather & Mood Summary\n");
        sb.append("----------------------------------\n");

        int day = 1;
        for (DayRecord r : records) {
            sb.append("Day ").append(day++).append(" → ");
            sb.append("Weather: ").append(r.weather).append(", ");
            sb.append("Mood: ").append(r.mood).append("\n");
        }

        return sb.toString();
    }
}
