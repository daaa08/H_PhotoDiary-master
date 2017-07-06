package com.example.da08.h_photodiary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.da08.h_photodiary.domain.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.da08.h_photodiary.R.id.imageView;
import static com.example.da08.h_photodiary.R.id.txtContent;
import static com.example.da08.h_photodiary.R.id.txtDate;
import static com.example.da08.h_photodiary.R.id.txtTitle;

/**
 * Created by Da08 on 2017. 7. 6..
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {
    List<Data> data = new ArrayList<>();
    private LayoutInflater inflater;

    public CustomAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Data> data){
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Data ddata = data.get(position);
        holder.title.setText(ddata.title);
        holder.setDate(convertLongToString(ddata.date));
        holder.setPosition(position);
        holder.img.setImg(ddata.imgUri);

    }
    private String convertLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }

    class Holder extends RecyclerView.ViewHolder{
        public int position;
        public TextView title, txtIdate;
        public ImageView img;

        public Holder(final View itemView){
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            txtIdate = (TextView)itemView.findViewById(R.id.txtIdate);
            img = (ImageView)itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ReadActivity.class);
                    intent.putExtra("LIST_POSITION", position);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setPosition(int position){
            this.position = position;
        }
        public void setDate(String date){
            txtIdate.setText(date + "");
        }

        public void setImg(){
            // 목록에서 넘어온 position 값을 이용해 상세보기 데이터를 결정
            Intent intent = getIntent();
            int position = intent.getIntExtra("LIST_POSITION", -1);

            if(position > -1){
                Data data = Ddata.list.get(position);
                // 이미지 세팅
                if(data.fileUriString != null && !"".equals(data.fileUriString)){
                    Glide.with(this)
                            .load(data.fileUriString)
                            .into(img);
                }
            }

        }
    }
}
