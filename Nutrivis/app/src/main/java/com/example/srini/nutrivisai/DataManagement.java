package com.example.srini.nutrivisai;

import android.util.Log;

import java.util.*;


import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.DocumentReference;
//import com.google.cloud.firestore.DocumentSnapshot;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.SetOptions;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;

public class DataManagement {

   public static HashMap pullFromDB(Firestore db, FirebaseUser user, String toUpload){
       DocumentReference docRef = db.collection("userData").document(user.getUid());
       ApiFuture<DocumentSnapshot> future = docRef.get();
       DocumentSnapshot document = null;
       try{
           document = future.get();
       } catch(Exception e){
           e.printStackTrace();
       }

       HashMap retMap = null;
       if (document.exists()) {
           retMap = document.toObject(HashMap.class);
           Log.d("___return value", retMap.toString());
       } else {
           Log.d("___FAILED QUery", "doesn't exist");
       }
       return retMap;
   }
   public static void pushToDB(Firestore db, FirebaseUser user, HashMap<String,Object> toUpload){
       DocumentReference docRef = db.collection("userData").document(user.getUid());
       toUpload.put("utc", new Date().getTime());

       ApiFuture<WriteResult> writeResult = docRef.set(db, SetOptions.merge());
       try {
           Log.d("__CONTENTS", "Update time : " + writeResult.get().getUpdateTime());
       } catch(Exception e){
           Log.d("__Failed upload", e.toString());
       }

   }
}


