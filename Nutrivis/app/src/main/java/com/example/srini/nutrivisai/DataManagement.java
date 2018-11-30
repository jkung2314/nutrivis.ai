package com.example.srini.nutrivisai;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class DataManagement extends Activity {
    public String TAG = "__DATA_MGMT";
    private FirebaseFirestore db;
    private FirebaseUser user;

    DataManagement(FirebaseUser mUser) {
        this.db = FirebaseFirestore.getInstance();
        this.user = mUser;
    }
    public void triggerMain(String uri, String relPath){
        Intent in = new Intent( this, MainActivity.class);
        in.putExtra("relPath", relPath);
        in.putExtra("uri", uri);
        startActivity(in);
    }

    public void pushFood(final Food f, final String relPath) {
        DocumentReference docRef = db.collection("userData").document(user.getUid());


        docRef.update("foods", FieldValue.arrayUnion(f.toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                triggerMain(f.getURL(),relPath );
            }
        });


    }

    // Jonathan, this was the only working pull code I had
    public void getDocument(FirebaseFirestore db, FirebaseUser user) {

        DocumentReference docRef = db.collection("userData").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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




