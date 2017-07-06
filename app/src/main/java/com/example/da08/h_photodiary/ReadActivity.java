package com.example.da08.h_photodiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.da08.h_photodiary.domain.Data;

import java.text.SimpleDateFormat;

import static android.R.id.content;
import static android.R.id.title;

public class ReadActivity extends AppCompatActivity {

    TextView txtTitle, txtDate, txtContent;
    ImageView imageView;
    Button btnDownload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtContent = (TextView)findViewById(R.id.txtContent);
        imageView = (ImageView)findViewById(R.id.imageView);
        btnDownload = (Button)findViewById(R.id.btnDownload);

        txtContent.setMovementMethod(new ScrollingMovementMethod());

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, content);

                Intent chooser = Intent.createChooser(intent, "공유");
                startActivity(chooser);
            }
        });

        setData();
    }

    public void setData(){
        // 목록에서 넘어온 position 값을 이용해 상세보기 데이터를 결정
        Intent intent = getIntent();
        int position = intent.getIntExtra("LIST_POSITION", -1);

        if(position > -1){
            Data data = Ddata.list.get(position);
            // 이미지 세팅
            if(data.fileUriString != null && !"".equals(data.fileUriString)){
                Glide.with(this)
                        .load(data.fileUriString)
                        .into(imageView);
            }
            // 값 세팅
            txtTitle.setText(data.title);
            txtContent.setText(data.content);
            txtDate.setText(convertLongToString(data.date));

        }

    }
    private String convertLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }



}
