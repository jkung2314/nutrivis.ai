package com.example.srini.nutrivisai;
import java.lang.String;
//import java.util.*;
//import java.lang.Object;
import android.os.AsyncTask;
import android.util.Log;

import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;


public class NutritionixTask {

    public String getNutritionInfo(String... foods) throws IOException {
        int count = foods.length;
        String result = "";
        for (int i = 0; i < count; i++) {
            String requestUrl = "https://trackapi.nutritionix.com/v2/natural/nutrients";
            URL url = new URL(requestUrl);
            HttpsURLConnection request = (HttpsURLConnection)url.openConnection();
            request.setRequestMethod("POST");
            //set the headers
            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            request.setRequestProperty("x-app-key", "fadcbeb5cfe80845c11760457710ff0d");
            request.setRequestProperty("x-app-id", "f19f035d");
            // Set query param
            String param = "query=" + foods[i];
            //required for POST, allows to write to the connection
            request.setDoOutput(true);
            //write out the post body
            OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
            out.write(param);
            out.close();
            //get the response in str
            BufferedReader responseStream = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str= "", temp;
            while(( temp = responseStream.readLine()) != null){
                str += temp;
            }
            responseStream.close();

            result += str + "\n";
        }
        return result;
    }

}

class NutritionixTaskCall extends AsyncTask<String, Void, String>{

    private AsyncRequester caller;

    public NutritionixTaskCall(AsyncRequester c){
        this.caller = c;
    }

    protected String doInBackground(String... foods){

        try{
            NutritionixTask task = new NutritionixTask();
            String result = task.getNutritionInfo(foods);
            return result;
        }catch(MalformedURLException e){
            System.err.println("malformed url excpetion occured " + e.getMessage());
            return null;
        }catch(ProtocolException e){
            System.err.println("protocol exception occured " + e.getMessage());
            return null;
        }catch(IOException e){
            System.err.println("io exception occured " + e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(String reply) {
        caller.onCompletedTask(reply);
    }
}
