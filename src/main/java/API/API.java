package API;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.gson.*;

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

    public class GeoLocation {
        double lat;
        double lon;
        String name;
        String country;
        String state;
    }

    public GeoLocation getCoordinates(String userLocation) {
        try {
            Map<String, String> env = EnvLoader.loadEnv("data/.env");
            String apiKey = env.get("OPENWEATHER_TOKEN");

            String encodedLocation = URLEncoder.encode(userLocation, "UTF-8");

            String url = "https://api.openweathermap.org/geo/1.0/direct?q="+encodedLocation+"&limit=1&appid="+apiKey;
            String json = get(url);

            GeoLocation[] results = new Gson().fromJson(json, GeoLocation[].class);

            if (results.length == 0) {
                throw new RuntimeException("Location not found!: "+userLocation);
            }
            
            return results[0];
        } catch (Exception e) {
            throw new RuntimeException("Geolocation failed", e);
        }
    }

    public static class openWeatherResponse {
        public Main main;
        public Weather[] weather;

        public static class Main {
            public double temp;
            public double feels_like;
            public int humidity;
        }

        public static class Weather {
            public String main;
            public String description;
            public String icon;
        }
    }

    public  openWeatherResponse getWeather(double lat, double lon) {
        try {
            Map<String, String> env = EnvLoader.loadEnv("data/.env");
            String apiKey = env.get("OPENWEATHER_TOKEN");

            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid="+apiKey;
            String json = get(url);

            return new Gson().fromJson(json, openWeatherResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("OW Failed response");
        }
    }

    public openWeatherResponse getWeatherByName(String userLocation) {
        GeoLocation loc = getCoordinates(userLocation);
        return getWeather(loc.lat, loc.lon);
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
