package API;

import java.util.*;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class geminiAPI {
        
    public String geminiResponse(String journalInput) {
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
