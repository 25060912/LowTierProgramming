package logic.Journal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class JournalPage {
    private static final String JOURNAL_FILE = "data/journals.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static Map<LocalDate, String> journals = new TreeMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public void run() {
        loadJournals();
        showJournalsPage();
    }

    private static void loadJournals() {
        File file = new File(JOURNAL_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            LocalDate currentDate = null;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DATE:")) {
                    if (currentDate != null) {
                        journals.put(currentDate, content.toString().trim());
                        content = new StringBuilder();
                    }
                    currentDate = LocalDate.parse(line.substring(5).trim(), DATE_FORMATTER);
                } else if (line.equals("---")) {
                    if (currentDate != null) {
                        journals.put(currentDate, content.toString().trim());
                        content = new StringBuilder();
                        currentDate = null;
                    }
                } else {
                    if (content.length() > 0) {
                        content.append("\n");
                    }
                    content.append(line);
                }
            }

            if (currentDate != null) {
                journals.put(currentDate, content.toString().trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading journals: " + e.getMessage());
        }
    }

    private static void saveJournals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JOURNAL_FILE))) {
            for (Map.Entry<LocalDate, String> entry : journals.entrySet()) {
                writer.write("DATE:" + entry.getKey().format(DATE_FORMATTER));
                writer.newLine();
                writer.write(entry.getValue());
                writer.newLine();
                writer.write("---");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving journals: " + e.getMessage());
        }
    }

    private static void showJournalsPage() {
        while (true) {
            System.out.println("\nJournals Page");
            System.out.println("===");
            System.out.println("Journal Dates");
            System.out.println("===");

            LocalDate today = LocalDate.now();
            List<LocalDate> dates = new ArrayList<>(journals.keySet());
            
            if (!dates.contains(today)) {
                dates.add(today);
            }
            
            Collections.sort(dates);

            for (int i = 0; i < dates.size(); i++) {
                LocalDate date = dates.get(i);
                String dateStr = date.format(DATE_FORMATTER);
                if (date.equals(today)) {
                    System.out.println((i + 1) + ". " + dateStr + " (Today)");
                } else {
                    System.out.println((i + 1) + ". " + dateStr);
                }
            }

            boolean todayHasJournal = journals.containsKey(today);
            if (todayHasJournal) {
                System.out.print("\nSelect a date to view journal, or edit the journal for today:\n> ");
            } else {
                System.out.print("\nSelect a date to view journal, or create a new journal for today:\n> ");
            }

            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > dates.size()) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }

                LocalDate selectedDate = dates.get(choice - 1);
                
                if (selectedDate.equals(today)) {
                    handleTodayJournal(today);
                } else {
                    viewJournal(selectedDate);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void handleTodayJournal(LocalDate today) {
        if (!journals.containsKey(today)) {
            createJournal(today);
        } else {
            showTodayOptions(today);
        }
    }

    private static void createJournal(LocalDate date) {
        System.out.println("\nEnter your journal entry for " + date.format(DATE_FORMATTER) + ":");
        System.out.print("> ");
        String entry = scanner.nextLine();
        
        journals.put(date, entry);
        saveJournals();
        
        System.out.println("\nJournal saved successfully!");
        System.out.println("Would you like to:");
        System.out.println("1. View Journal");
        System.out.println("2. Edit Journal");
        System.out.println("3. Back to Dates");
        System.out.print("> ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                viewJournal(date);
                break;
            case "2":
                editJournal(date);
                break;
            case "3":
            default:
                break;
        }
    }

    private static void editJournal(LocalDate date) {
        System.out.println("\nEdit your journal entry for " + date.format(DATE_FORMATTER) + ":");
        System.out.print("> ");
        String entry = scanner.nextLine();
        
        journals.put(date, entry);
        saveJournals();
        
        System.out.println("\nJournal updated successfully!");
    }

    private static void showTodayOptions(LocalDate today) {
        System.out.println("\nWould you like to:");
        System.out.println("1. View Journal");
        System.out.println("2. Edit Journal");
        System.out.println("3. Back to Dates");
        System.out.print("> ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                viewJournal(today);
                break;
            case "2":
                editJournal(today);
                break;
            case "3":
            default:
                break;
        }
    }

    private static void viewJournal(LocalDate date) {
        System.out.println("\n=== Journal Entry for " + date.format(DATE_FORMATTER) + " ===");
        
        String entry = journals.get(date);
        if (entry != null && !entry.isEmpty()) {
            System.out.println(entry);
        } else {
            System.out.println("No journal entry for this date.");
        }
        
        System.out.println("\nPress Enter to go back.");
        System.out.print("> ");
        scanner.nextLine();
    }
}