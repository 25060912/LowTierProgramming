package UI;

import API.*;

public class ghazy {

    geminiAPI api = new geminiAPI();

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
        String prompt = "Reply with the lyrics of Eminem Lose Yourself";
        String response = api.geminiResponse(prompt, journal);

        if (response == null) {
            return "Error bang";
        }

        if (response.length() == 0) {
            return "Error bang";
        }

        else return response;

    }
}