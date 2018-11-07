package com.example.srini.nutrivisai;

import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.gson.Gson;

import org.json.JSONObject;


import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;


public class GVision {

    Vision vision;
    private static String apiKeyPath;

    GVision(String apiKey) {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                jsonFactory,
                null).setApplicationName("Nutrivis.ai");
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(apiKey));

        vision = visionBuilder.build();
    }

    public List<AnnotateImageResponse> getFeatures(String filePath) throws IOException {
        //Convert image to base64
        Path path = Paths.get(filePath);
        byte[] imgData = Files.readAllBytes(path);

        //Set image request headers
        Feature feature = new Feature();
        feature.setType("LABEL_DETECTION");
        AnnotateImageRequest request = new AnnotateImageRequest();
        Image image = new Image();
        image.encodeContent(imgData);
        request.setImage(image);
        request.setFeatures(Arrays.asList(feature));

        //Send request and get response
        try {
            BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
            batchRequest.setRequests(Arrays.asList(request));
            BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();

            //Read response
            List<AnnotateImageResponse> responses = batchResponse.getResponses();
            return responses;
        } catch (GoogleJsonResponseException e) {
            Log.wtf("GVision Authentication", "Authentication error caused by ".concat(e.getMessage()));
            return null;
        }

    }

    public static String callGVis(final String filePath) {
        String jsonResponse = "NOTHIN'";
        Thread thread = new Thread(new Runnable() {
            public void run() {

                try {
                    apiKeyPath = "GVis_key.txt";
                    GVision viz = new GVision("AIzaSyAdtGrbdqnRbMgMCsDtf5gHXyaqA-v2Lgo");
                    List<AnnotateImageResponse> response = viz.getFeatures(filePath);
                    Gson gson = new Gson();

                    String jsonResp = gson.toJson(response.get(0));
                    Log.d("__GSON_RESULT_RAW", jsonResp);

/* Half of someones attempt to parse gson

                    JSONObject object = new JSONObject(jsonResp);
                    Iterator<?> keys = object.keys();
                    while(keys.hasNext() ) {
                        String key = (String)keys.next();
                        if ( object.get(key) instanceof JSONObject ) {
                            JSONObject element = new JSONObject(object.get(key).toString());
                            Log.d("res1",element.getString("description"));
                            Log.d("res2",element.getString("score"));
                        }
                    }
*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        return jsonResponse;

    }
}
/* Teting main, add your own path to a file
public static void main(String args[]){
        String resp = callGVis("/Users/daniel/projects/nutrivis.ai/Nutrivis/app/src/main/java/com/example/srini/nutrivisai/food.jpeg");
        System.out.println(resp);
}
*/



