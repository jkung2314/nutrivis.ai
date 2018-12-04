package com.example.srini.nutrivisai;

import android.app.Application;
import android.graphics.Bitmap;
import android.app.Activity;
import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;


public class CameraActivity extends AppCompatActivity{

    final static int REQUEST_CODE = 1;
    private ImageView image;
    private String mCurrentPhotoPath;
    private Intent takePictureIntent;
    private FirebaseUser user;
    private String photoDBPath;
    public Uri photoUri = new Uri.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

       user = (FirebaseUser)bundle.get("user");
        Log.e("_______", user.getDisplayName());

        setContentView(R.layout.activity_camera);

        dispatchTakePictureIntent();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Bundle extras = takePictureIntent.getExtras();
            Bundle userInfo = getIntent().getExtras();
            Bitmap data = (Bitmap) extras.get("data");
            image = (ImageView) findViewById(R.id.img);
            image.setImageBitmap(data);

            // wherever this needs to be redirected
            Intent i = new Intent(this, DataManagement.class);
            i.putExtra("photoPath", mCurrentPhotoPath);
            i.putExtra("user", user);
            startActivity(i);

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("__photo path", mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        }
        galleryAddPic();

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    }


