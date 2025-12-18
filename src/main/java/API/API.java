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

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("GET failed. HTTP error code: " + conn.getResponseCode());
        }

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
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.substring(1))
              .append(" ");
        }
        return sb.toString().trim();
    }

    // =========================
    // WEATHER API (MALAYSIA + GLOBAL)
    // =========================
    public String weatherAPI(String userLocation) {

        String malaysiaWeather = null;

        // ---------- 1️⃣ MALAYSIA WEATHER ----------
        try {
            userLocation = capitalizeFirstLetter(userLocation);
            String encodedLocation = URLEncoder.encode(userLocation, "UTF-8");

            String getUrl =
                "https://api.data.gov.my/weather/forecast/?contains="
                + encodedLocation
                + "@location__location_name&sort=date&limit=1";

            String getResponse = get(getUrl);
            JSONArray forecastArray = new JSONArray(getResponse);

            if (forecastArray.length() > 0) {
                JSONObject forecast = forecastArray.getJSONObject(0);
                malaysiaWeather = forecast.getString("summary_forecast");
            }
        } catch (Exception e) {
            malaysiaWeather = null;
        }

        // Kalau Malaysia ada data → terus return
        if (malaysiaWeather != null) {
            return "MY Weather: " + malaysiaWeather;
        }

        // ---------- 2️⃣ OPENWEATHER (GLOBAL FALLBACK) ----------

        Map<String, String> env = EnvLoader.loadEnv("data/.env");

        try {
            String apiKey = env.get("OPENWEATHER_API_KEY");
            if (apiKey == null) return null;

            String encodedLocation = URLEncoder.encode(userLocation, "UTF-8");

            String url =
                "https://api.openweathermap.org/data/2.5/weather?q="
                + encodedLocation
                + "&appid=" + apiKey
                + "&units=metric";

            String response = get(url);
            JSONObject json = new JSONObject(response);

            JSONObject main = json.getJSONObject("main");
            double temp = main.getDouble("temp");

            JSONArray weatherArr = json.getJSONArray("weather");
            String desc = weatherArr.getJSONObject(0).getString("description");

            return "Global Weather: " + temp + "°C, " + desc;

        } catch (Exception e) {
            return null;
        }
    }

    // =========================
    // GEMINI SENTIMENT ANALYSIS
    // =========================
    public String geminiAPI(String journalInput) {

        Map<String, String> env = EnvLoader.loadEnv("data/.env");

        try {
            Client client = Client.builder()
                    .apiKey(env.get("GEMINI_TOKEN"))
                    .build();

            String prompt =
                "Analyze the sentiment of this sentence, return either POSITIVE or NEGATIVE only.\n"
                + journalInput;

            GenerateContentResponse responseGemini =
                client.models.generateContent("gemini-2.5-flash", prompt, null);

            client.close();
            return "Gemini Response: " + responseGemini.text();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
