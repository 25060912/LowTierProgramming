import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

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

    /**
     * Sends a POST request with JSON body and Bearer token authentication.
     * 
     * @param apiURL      the URL to send the POST request to
     * @param bearerToken the bearer token for Authorization header
     * @param jsonBody    the JSON payload as a string
     * @return the response body as a String
     * @throws Exception if the request fails
     */
    public String post(String apiURL, String bearerToken, String jsonBody) throws Exception {
        URL url = new URL(apiURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set HTTP method and headers
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

        // Enable sending body
        conn.setDoOutput(true);

        // Write request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Check for success
        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 201) {
            throw new RuntimeException("POST failed. HTTP error code: " + responseCode);
        }

        // Read response
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder sb = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            sb.append(responseLine.trim());
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

            // --- Example POST request: Perform sentiment analysis using HuggingFace model ---
            String journalInput = "I spent my free time with my friends today. We had a great time at the park and enjoyed the sunny weather.";
            String postUrl = "https://router.huggingface.co/hf-inference/models/distilbert/distilbert-base-uncased-finetuned-sst-2-english";

            // Safely get bearer token
            String bearerToken = env.get("BEARER_TOKEN");
            if (bearerToken == null || bearerToken.isEmpty()) {
                System.err.println("Error: BEARER_TOKEN is not set in the environment.");
                return;
            }

            // Format JSON body
            String jsonBody = "{\"inputs\": \"" + journalInput + "\"}";

            // Call POST
            String postResponse = api.post(postUrl, bearerToken, jsonBody);

            JSONArray outerArray = new JSONArray(postResponse);
            JSONArray innerArray = outerArray.getJSONArray(0);
            JSONObject topResult = innerArray.getJSONObject(0);
            String topLabel = topResult.getString("label");
            
            System.out.println("Sentiment Analysis Response: "+topLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
