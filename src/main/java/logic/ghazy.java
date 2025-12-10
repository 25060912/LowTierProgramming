package logic;
import API.API;
public class ghazy {
    API api = new API();

    public String analyze(String journal) {
        String response = api.geminiAPI(journal);

        if (response == null) return "Error bang";

        response = response.toUpperCase();

        if (response.contains("POSITIVE")) {
            return "Your journal positive bang!";
        }

        if (response.contains("NEGATIVE")) {
            return "Your journal negative bang!";
        }

        return "Error bang";    
    }

}