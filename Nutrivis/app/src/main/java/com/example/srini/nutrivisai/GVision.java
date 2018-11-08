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

import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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
        // Limit threads and make returnable with executor
        ExecutorService es = Executors.newFixedThreadPool(2);
        Future<HashMap> preds = es.submit(new asyncCall(filePath));

        try{
            Log.d("__callGvis", preds.get().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        es.shutdown();

        return preds.toString();
    }

    /* Teting main, add your own path to a file
    public static void main(String args[]) throws Exception {
        callGVis("/Users/daniel/projects/nutrivis.ai/Nutrivis/app/src/main/java/com/example/srini/nutrivisai/food.jpeg");

    }
    */
}
    class asyncCall implements Callable<HashMap> {
        private volatile HashMap<String, Float> predictions;
        private final String filePath;
        asyncCall(String fp){
            filePath = fp;
            getPredictions();

        }

        public void getPredictions() {

            try {
                GVision viz = new GVision("AIzaSyAdtGrbdqnRbMgMCsDtf5gHXyaqA-v2Lgo");
                List<AnnotateImageResponse> response = viz.getFeatures(filePath);
                Gson gson = new Gson();

                String jsonResp = gson.toJson(response.get(0));
                predictions = new HashMap();

                // split up json into string arrays
                String[] descriptions = jsonResp.split("\"description\":\"");
                String[] scores = jsonResp.split("score\":");
                for (int i = 0; i < 5; i++) {

                        String desc = descriptions[i].split("\"")[0];  // take off the ends of the string
                        String score = scores[i].split(",")[0];

                        // If the desc or score contains any non-alpha chars, skip
                        if (! desc.matches(".*[a-z].*")) {
                            Log.d("__asyncCall","INVALID desc  " + desc);
                            Log.d("__asyncCall","INVALID score  " + score);
                            continue;
                        }
                        Log.d("__asyncCall","desc  " + desc);
                        Log.d("__asyncCall","score  " + score);
                        predictions.put(desc, Float.valueOf(score));  //add to hashmap
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public HashMap<String, Float> call(){
            return predictions;
        }
    }



