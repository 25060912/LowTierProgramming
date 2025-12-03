import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public String get(String apiURL) throws Exception {
        URL url = new URL(apiURL);
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

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    // Example usage
    public static void main(String[] args) {
        API api = new API();

        // Load environment variables from .env file (custom loader)
        Map<String, String> env = EnvLoader.loadEnv("data/.env");

        try {
            // --- Example GET request: Fetch latest weather forecast for Kuala Lumpur ---
            Scanner sc = new Scanner(System.in);
            String userLocation = sc.nextLine();
            userLocation = capitalizeFirstLetter(userLocation);

            String encodedLocation = URLEncoder.encode(userLocation, "UTF-8");
            String getUrl = "https://api.data.gov.my/weather/forecast/?contains="+encodedLocation+"@location__location_name&sort=date&limit=1";
            String getResponse = api.get(getUrl);
            
            JSONArray forecastArray = new JSONArray(getResponse);
            JSONObject forecast = forecastArray.getJSONObject(0);
            String summaryForecast = forecast.getString("summary_forecast");
            System.out.println("GET Response: "+summaryForecast);

            // ATTEMPTING GEMINI CALLS INSTEAD OF HUGGINGFACE
            Client client = Client.builder().apiKey(env.get("GEMINI_TOKEN")).build();
            
            String journalInput = sc.nextLine();
            String prompt = "Analyze the sentiment of this sentence, return either POSITIVE or NEGATIVE only. Attach the word SARCASM in parentheses next to the return values if applicable.\n"+journalInput;

            GenerateContentResponse responseGemini = client.models.generateContent("gemini-2.5-flash", prompt, null);
            System.out.println("Gemini response: "+responseGemini.text());
            sc.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
