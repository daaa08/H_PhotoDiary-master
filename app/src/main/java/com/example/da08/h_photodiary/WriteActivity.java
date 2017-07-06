package com.example.da08.h_photodiary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button btnSave, btnGallery;
    TextView txtImagename;

    FirebaseDatabase database;
    DatabaseReference dogRef;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        // database 레퍼런스
        database = FirebaseDatabase.getInstance();
        dogRef = database.getReference("dog");
        // storage 레퍼런스
        mStorageRef = FirebaseStorage.getInstance().getReference("images");

        editTitle = (EditText)findViewById(R.id.editTitle);
        editContent = (EditText)findViewById(R.id.editContent);
        btnSave  = (Button)findViewById(R.id.btnSave);
        btnGallery  = (Button)findViewById(R.id.btnGallery);
        txtImagename = (TextView)findViewById(R.id.txtImagename);
    }

    public void upLoadFile(String filePath){
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        String fileName = file.getName();
        StorageReference riversRef = mStorageRef.child(fileName);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        // 파이어 베이스 스토리지에 방금 업로드한 파일의 경로
                        @SuppressWarnings("VisibleForTests")
                        Uri upLoadedUri = taskSnapshot.getDownloadUrl();
                        afterUploadFile(upLoadedUri);
                        Log.e("storage","success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.e("storage","fail"+exception.getMessage());
                    }
                });
    }

    public void postData(View v ){

        String imgPath = txtImagename.getText().toString();
        if(imgPath != null && !"".equals(imgPath)){
            upLoadFile(imgPath);
        }else{
            afterUploadFile(null);
        }

    }

    public void afterUploadFile(Uri imageUri){

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        Date date = new Date();
        long listdate = date.getTime();

        Data data = new Data(title,content,listdate);

        if(imageUri != null){
            data.fileUriString = imageUri.toString();
//            data.imgUri = imageUri;
        }
        String dogKey = dogRef.push().getKey();
        dogRef.child(dogKey).setValue(data);
        finish();
    }

    public void openGallery(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult( Intent.createChooser(intent, "앱을 선택하세요") , 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case 100:
                    Uri imageUri = data.getData();
                    String filePath = getPsthFromUri(this,imageUri);
                    txtImagename.setText(filePath);
                    break;
            }
        }
    }

    public static String getPsthFromUri(Context context, Uri uri){
        String realPath = "";
        Cursor cu = context.getContentResolver().query(uri,null,null,null,null);
        if(cu.moveToNext()){
            realPath = cu.getString(cu.getColumnIndex("_data"));
        }
        cu.close();
        return realPath;
    }
}
