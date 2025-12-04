package API;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class API {

    /**
     * Sends a GET request to the specified API URL.
     * 
     * @param apiURL the URL to send the GET request to
     * @return the response body as a String
     * @throws Exception if the request fails
     */
    private String get(String apiURL) throws Exception {
        URI uri = new URI(apiURL);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set HTTP method and headers
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        // Check for successful response
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("GET failed. HTTP error code: " + conn.getResponseCode());
        }

        // Read response
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    // Example usage
    public String weatherAPI(String userLocation) {
        API api = new API();
        try {
            // --- Example GET request: Fetch latest weather forecast for Kuala Lumpur ---
            userLocation = capitalizeFirstLetter(userLocation);
            String encodedLocation = URLEncoder.encode(userLocation, "UTF-8");

            String getUrl = "https://api.data.gov.my/weather/forecast/?contains="+encodedLocation+"@location__location_name&sort=date&limit=1";
            String getResponse = api.get(getUrl);
            
            JSONArray forecastArray = new JSONArray(getResponse);
            JSONObject forecast = forecastArray.getJSONObject(0);

            return "GET response: "+forecast.getString("summary_forecast");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        
    public String geminiAPI(String journalInput) {
        // Load environment variables from .env file (custom loader)
        Map<String, String> env = EnvLoader.loadEnv("data/.env");

        try {
            // ATTEMPTING GEMINI CALLS INSTEAD OF HUGGINGFACE
            Client client = Client.builder().apiKey(env.get("GEMINI_TOKEN")).build();
            
            String prompt = "Analyze the sentiment of this sentence, return either POSITIVE or NEGATIVE only. Attach the word SARCASM in parentheses next to the return values if applicable.\n"+journalInput;
            GenerateContentResponse responseGemini = client.models.generateContent("gemini-2.5-flash", prompt, null);
            
            client.close();
            return "Gemini Response: "+responseGemini.text();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
