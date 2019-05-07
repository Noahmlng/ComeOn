package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.comeon.android.R;

/**
 * 体育场馆信息适配器
 */
public class StadiumsAdapter extends RecyclerView.Adapter<StadiumsAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //每个item中的控件
        ImageView img_stadium;
        TextView txt_stadiumName;
        TextView txt_stadiumDistrict;
        TextView txt_stadiumType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_stadium=(ImageView)itemView.findViewById(R.id.img_stadium);
            txt_stadiumName=(TextView)itemView.findViewById(R.id.txt_stadiumName);
            txt_stadiumDistrict=(TextView)itemView.findViewById(R.id.txt_stadiumDistrict);
            txt_stadiumType=(TextView)itemView.findViewById(R.id.txt_stadiumType);
        }
    }
}
