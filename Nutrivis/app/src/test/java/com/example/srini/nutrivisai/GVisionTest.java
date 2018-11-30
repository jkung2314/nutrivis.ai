package com.example.srini.nutrivisai;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GVisionTest {

    private GVision viz;
    private List<AnnotateImageResponse> response;

    @Before
    public void setUp() throws Exception {
        System.out.println("Beginning GVision Unit + Integration Tests!");
        //Find better way to insert apiKey in the future
        viz = new GVision("AIzaSyAdtGrbdqnRbMgMCsDtf5gHXyaqA-v2Lgo");
    }

    @Test
    public void verifyGVisionIntegrity() {
        assertNotNull(viz.toString());
        assertEquals("https://vision.googleapis.com", viz.vision.getBaseUrl());
    }

    @Test
    public void getImgFeatures() throws IOException {
        //Get file path and get response
        File img = new File(getClass().getResource("/maxresdefault.jpg").getPath());
        String filePath = img.getAbsolutePath();
        response = viz.getFeatures(filePath);

        //Model generates different scores each time, so can't assertEquals
        assertNotNull(response.get(0));
    }

    @Test
    public void verifyJSONResponse() throws IOException {
        //Get response
        File img = new File(getClass().getResource("/maxresdefault.jpg").getPath());
        String filePath = img.getAbsolutePath();
        response = viz.getFeatures(filePath);

        //Parse response into map
        HashMap predictions = new HashMap();
        Gson gson = new Gson();
        String jsonResp = gson.toJson(response.get(0));
        predictions = GVision.parse(jsonResp, predictions);
        System.out.println(predictions.toString());

        //Verify map
        assertEquals(4, predictions.size());
        assertEquals(true, predictions.containsKey("golden retriever"));
        assertEquals(true, predictions.containsKey("dog breed"));
        assertEquals(true, predictions.containsKey("dog"));
        assertEquals(true, predictions.containsKey("dog like mammal"));
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Finished GVision Unit + Integration Tests!");
    }

}