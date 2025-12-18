package logic;
import API.API;
public class ghazy {
    API api = new API();

         public String analyze(String journal) {

        // =========================
        // Input validation
        // =========================
        if (journal == null) {
            return "Error bang";
        }

        if (journal.length() == 0) {
            return "Error bang";
        }

        // =========================
        // Call Gemini API
        // =========================
        String response = api.geminiAPI(journal);

        if (response == null) {
            return "Error bang";
        }

        if (response.length() == 0) {
            return "Error bang";
        }

        // =========================
        // Normalize response
        // =========================
        response = response.toUpperCase();

        // =========================
        // Sentiment analysis
        // =========================
        if (response.contains("POSITIVE")) {
            return "Your journal positive bang!";
        }

        if (response.contains("NEGATIVE")) {
            return "Your journal negative bang!";
        }

        // =========================
        // Default
        // =========================
        return "Error bang";
    }
}