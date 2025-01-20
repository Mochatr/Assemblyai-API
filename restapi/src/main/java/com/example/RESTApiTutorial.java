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
    }
}
