package com.example.da08.h_photodiary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.da08.h_photodiary.domain.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by Da08 on 2017. 7. 6..
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {
    List<Data> list = new ArrayList<>();
    private LayoutInflater inflater;

    public CustomAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Data> list){
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Data data = list.get(position);
        holder.txtTitle.setText(data.title);
        holder.setDate(convertLongToString(data.date));
        holder.setPosition(position);

    }
    private String convertLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }

    class Holder extends RecyclerView.ViewHolder{
        private int position;
        private TextView txtTitle,txtDate;

        public Holder(final View itemView){
            super(itemView);
            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);

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
            txtDate.setText(date + "");
        }
    }
}
