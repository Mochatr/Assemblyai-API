package com.example; 

import java.net.URI;
import java.net.http.*;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import com.example.Transcript;

public class RESTApiTutorial {
    public static void main( String[] args ) throws Exception {

        Dotenv dotenv = Dotenv.load();
        String API_KEY = dotenv.get("API_KEY");

        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");

        // create gson object
        Gson gson = new Gson();
        // convert transcript object to json string
        String jsonRequest = gson.toJson(transcript);

        System.out.println(jsonRequest);

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI("https://api.assemblyai.com/v2/transcript"))
            .header("Authorization", API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        
        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);
        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
            .header("Authorization", API_KEY)
            .GET()
            .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);

            System.out.println(transcript.getStatus());

            if (transcript.getStatus().equals("completed") || transcript.getStatus().equals("error")) {
                break;
            }
            // Wait for a second before checking the status again
            Thread.sleep(1000);
        }
        
        System.out.println("Transcription complete!");
        System.out.println(transcript.getText());
    }
}
