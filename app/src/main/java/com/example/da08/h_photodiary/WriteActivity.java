package com.example.da08.h_photodiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.da08.h_photodiary.domain.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    EditText editTitle, editContent;
    TextView txtImagename;

    FirebaseDatabase database;
    DatabaseReference dogRef;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        database = FirebaseDatabase.getInstance();
        dogRef = database.getReference("dog");

        mStorageRef = FirebaseStorage.getInstance().getReference("images");

        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);
        txtImagename = (TextView) findViewById(R.id.txtImagename);

    }

    public void postData(View view) {
        String imagePath = txtImagename.getText().toString();
        if(imagePath != null && !"".equals(imagePath)){
            upLoadFile(imagePath);
        }else{
            afterUploadFile(null);
        }

    }
    public void upLoadFile(String filePath) {
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        String fileName = file.getName();
        StorageReference fileRef = mStorageRef.child(fileName);
        fileRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests")
                        Uri uploadedUri = taskSnapshot.getDownloadUrl();
                        afterUploadFile(uploadedUri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("FBStorage", "Upload Fail:" + exception.getMessage());
                    }
                });
    }

    public void afterUploadFile(Uri imageUri){
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        Date date = new Date();
        long diaryDate = date.getTime();

        Data data = new Data(title, content, diaryDate);
        if(imageUri !=  null){
            data.fileUriString = imageUri.toString();
        }
        String dogKey = dogRef.push().getKey();
        dogRef.child(dogKey).setValue(data);

        finish();
    }

    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "앱을 선택하세요"), 100);

    }
}
