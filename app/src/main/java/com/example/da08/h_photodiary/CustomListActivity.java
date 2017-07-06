package com.example.da08.h_photodiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.da08.h_photodiary.domain.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference dataRef;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomListActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference("dog");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        dialog = new ProgressDialog(this);
//        dialog.setTitle("게시글 불러오는중..");
//        dialog.setMessage("ing...");
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.show();

        loadData();
    }

    public CustomListActivity(){

    }
    public void loadData(){
        Ddata.list.clear();
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    // json 데이터를 Bbs 인스턴스로 변환오류 발생 가능성 있음
                    // 그래서 예외처리 필요
                    try {
                        Data data = item.getValue(Data.class);
                        Ddata.list.add(data);
                    }catch(Exception e){
                        Log.e("Firebase",e.getMessage());
                    }
                }
                refreshList(Ddata.list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void refreshList(List<Data> data){
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

}
