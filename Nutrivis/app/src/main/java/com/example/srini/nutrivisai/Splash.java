package com.example.srini.nutrivisai;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Splash extends AppCompatActivity {


    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    public Splash t;
    String GOOGLE_TOS_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t=this;
        setContentView(R.layout.activity_splash);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, AuthUiActivity.class));
            finish();
            return;
        } else {
            String mUsername = mFirebaseUser.getDisplayName();
            String mEmailAddress = mFirebaseUser.getEmail();
            Log.d("__login user", mUsername + "    " + mEmailAddress);

            getUserData(mFirebaseUser);
        }
    }

    public void triggerMain(ArrayList<String> docs){
        Intent in = new Intent(this, MainActivity.class);
        in.putExtra("docs", docs);
        startActivity(in);
    }

    public void createUser(DocumentReference docref){
        HashMap data = new HashMap();
        data.put("name", mFirebaseUser.getDisplayName());
        docref.set(data).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                getUserData(mFirebaseUser);
            }
            });
        }


    public String TAG = "__getUserData";
    public void getUserData(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            final DocumentReference docRef = db.collection("userData").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.get("foods"));
                            triggerMain( (ArrayList<String>) document.get("foods"));

                        } else {
                            Log.e(TAG, "No such document");
                            createUser(docRef);
                        }
                    } else {
                        Log.e(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }


    }
