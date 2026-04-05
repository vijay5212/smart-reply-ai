////package com.example.replaytool;
////
////import org.springframework.web.bind.annotation.*;
////
////import java.net.URI;
////import java.net.http.HttpClient;
////import java.net.http.HttpRequest;
////import java.net.http.HttpResponse;
////
///import com.fasterxml.jackson.databind.ObjectMapper;
////import java.util.*;
////
////@RestController
////@CrossOrigin(origins = "*")
////@RequestMapping("/api")
////public class ReplyController {
////
////    private static final String API_KEY = "AIzaSyCb8lYNdPPmVJWWjZWqZZzedlw4Dy7ICU0"; // use env variable
////
////    @PostMapping("/rewrite")
////    public String rewriteMessage(@RequestBody String message) {
////        try {
////            String prompt = "Rewrite the message in a professional, polite, and human tone. Generate 5 variations:\n\n" + message;
////
////            // Create JSON safely
////            Map<String, Object> body = new HashMap<>();
////            body.put("model", "gemini‑ultra");
////
////            List<Map<String, String>> messages = new ArrayList<>();
////            Map<String, String> msg = new HashMap<>();
////            msg.put("role", "user");
////            msg.put("content", prompt);
////
////            messages.add(msg);
////            body.put("messages", messages);
////
////            ObjectMapper mapper = new ObjectMapper();
////            String requestBody = mapper.writeValueAsString(body);
////
////           /* HttpRequest request = HttpRequest.newBuilder()
////                    .uri(new URI("https://generativelanguage.googleapis.com/v1beta2/models/gemini-ultra:generateText?key=" + API_KEY))
////                  //  .header("Authorization", "Bearer " + API_KEY)
////                    .header("Content-Type", "application/json")
////                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
////                    .build();*/
////            HttpRequest request = HttpRequest.newBuilder()
////                    .uri(new URI("https://generativelanguage.googleapis.com/v1beta2/models/gemini-ultra:generateText?key=" + API_KEY))
////                    .header("Content-Type", "application/json")
////                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
////                    .build();
////
////            HttpClient client = HttpClient.newHttpClient();
////            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
////
////            return response.body();
////
////        } catch (Exception e) {
////            return "Error: " + e.getMessage();
////        }
////    }
////}
//package com.example.replaytool;
//
//import org.springframework.web.bind.annotation.*;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/api")
//public class ReplyController {
//
//    private static final String API_KEY = "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"; // safer
//
//    @PostMapping("/rewrite")
//    public String rewriteMessage(@RequestBody String message) {
//        try {
//            String promptText = "Rewrite the message in a professional, polite, and human tone. Generate 5 variations:\n\n" + message;
//
//            // Create JSON body for Gemini
//            Map<String, Object> promptMap = new HashMap<>();
//            promptMap.put("text", promptText);
//
//            Map<String, Object> body = new HashMap<>();
//            body.put("prompt", promptMap);
//            body.put("temperature", 0.7);
//            body.put("candidate_count", 5);
//
//            ObjectMapper mapper = new ObjectMapper();
//            String requestBody = mapper.writeValueAsString(body);
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("https://generativelanguage.googleapis.com/v1beta2/models/gemini-ultra:generateText?key=" + API_KEY))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                    .build();
//
//            HttpClient client = HttpClient.newHttpClient();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            return response.body();
//
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//}

package com.example.replaytool;

import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ReplyController {

    private static final String API_KEY = "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";

    @PostMapping("/rewrite")
    public String rewriteMessage(@RequestBody String message) {
        try {

            String prompt = "Rewrite the message in a professional, polite, and human tone. Generate 5 variations:\n\n" + message;

            String requestBody = """
            {
              "model": "openai/gpt-4o-mini",
              "messages": [
                {
                  "role": "system",
                  "content": "You are a professional customer support assistant. Always reply politely, clearly, and briefly. Use professional English. Never be rude. Give 5-10 variations. Keep responses short and human-like."
                },
                {
                  "role": "user",
                  "content": "%s"
                }
              ]
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://openrouter.ai/api/v1/chat/completions"))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .header("HTTP-Referer", "http://localhost:8081")
                    .header("X-Title", "ReplayTool")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

           // return response.body();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            String result = json
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

            return result;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}