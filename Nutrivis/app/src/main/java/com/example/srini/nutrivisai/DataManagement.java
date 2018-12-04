package com.example.srini.nutrivisai;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.util.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class DataManagement extends AppCompatActivity implements AsyncRequester {
    public String TAG = "__DATA_MGMT";
    private FirebaseFirestore db;
    private FirebaseUser user;
    public String uri;
    private String photoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamgmt);

        Bundle bundle = getIntent().getExtras();

        this.user = (FirebaseUser)bundle.get("user");
        Log.e("_______", user.getDisplayName());

        this.db = FirebaseFirestore.getInstance();
        photoPath = (String)bundle.get("photoPath");
        if(photoPath != null){
            uploadFood();
        }

    }


    public void pushFood(final Food f, final String relPath) {
        DocumentReference docRef = db.collection("userData").document(user.getUid());

        uri = f.getURL();
        docRef.update("foods", FieldValue.arrayUnion(f.toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent in = new Intent( getApplicationContext(), Splash.class);
                startActivity(in);
            }
        });


    }
    @Override
    public void onCompletedTask(Map m){
        Intent in = new Intent( this, MainActivity.class);
        in.putExtra("newFood", "NULL");

        startActivity(in);
    }
    @Override
    public void onCompletedTask(String f){
        Intent in = new Intent( this, MainActivity.class);
        in.putExtra("newFood", f);

        startActivity(in);
    }

    private void uploadFood(){
        HashMap map = GVision.callGVis(photoPath);
        Log.d("TAG", "Map of preds: " + Arrays.asList(map));

        ResolveFood rf = new ResolveFood(this);
        String finalFood = rf.resolveFood(map);
        Log.d("TAG", "Final Food: " + finalFood);
        AsyncTask<String, Void, String> nutrition = new NutritionixTaskCall(this).execute(finalFood);
        try {
            String n = nutrition.get();
            // f = NutritionixParser.parse(n);
            Food f = new Food("Pizz",30.0, 2000.0, " https://cdn.cnn.com/cnnnext/dam/assets/171027052520-processed-foods-exlarge-tease.jpg");

            uploadPhotoToStorage(f, photoPath);


        } catch (Exception e ) {

            e.printStackTrace();
        }
    }

    private void getUri(StorageReference storageRef, final Food f, final String relPath) {

        storageRef.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "COULD NOT FIND URL");
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri photoUri) {
                Log.d(TAG, "URL: " + photoUri.toString());
                // add values to food map if empty, FOR TESTING
                // eventually, this will be replaced with full map from NX
//                if (food.isEmpty()) {
//                    food.put("Fat", "0 grams");
//                    food.put("Sodium", "0 gram");
//                    food.put("Carbs", "0 grams");
//                    food.put("Name", "test pic");
//                }
//                food.put("uri", photoUri.toString());
//                // push food with new uri
                f.setUrl(photoUri.toString());
                pushFood(f, relPath);
            }
        });

    }

    public void uploadPhotoToStorage(final Food f, String mCurrentPhotoPath) {
//        mCurrentPhotoPath = "/Users/daniel/tmp/nutrivis.ai/Nutrivis/app/src/main/java/com/example/srini/nutrivisai/food.jpeg";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(mCurrentPhotoPath));

        final StorageReference uploadRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = uploadRef.putFile(file);
        final String photoDBPath = "images/" + file.getLastPathSegment();

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "NO PHOTO WAS UPLOADED");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "PHOTO " + photoDBPath + " WAS UPLOADED!");
                getUri(uploadRef, f, photoDBPath);
            }
        });

/* GVIS call not implemented
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Log.d("__RESPONSE", GVision.callGVis(mCurrentPhotoPath).toString());
        } catch (Exception ex) {
            Log.d("__RESPONSE_FAIL", ex.toString());
        }
*/

    }
}




