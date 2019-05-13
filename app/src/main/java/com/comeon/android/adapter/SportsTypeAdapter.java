package com.comeon.android.adapter;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeon.android.LaunchOrderActivity;
import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.StartActivity;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.UserInfo;
import com.comeon.android.fragment.LaunchOrderFragment;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;

import java.util.List;

/**
 * 运动类型适配器
 */
public class SportsTypeAdapter extends RecyclerView.Adapter<SportsTypeAdapter.ViewHolder> {

    private List<SportsType> sportsTypes;

    public SportsTypeAdapter(List<SportsType> sportsTypes){this.sportsTypes=sportsTypes;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sportstype_item, viewGroup, false);
        final ViewHolder viewHolder=new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SportsType sportsType=sportsTypes.get(viewHolder.getAdapterPosition());
                //传递数据到下一个页面
                Intent intent=new Intent(MyApplication.getContext(), LaunchOrderActivity.class);
                intent.putExtra("selectedSportsType",sportsType);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SportsType sportsType=sportsTypes.get(i);
        viewHolder.txt_sportsType.setText(sportsType.getTypeName());
    }

    @Override
    public int getItemCount() {
        return sportsTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_sportsType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_sportsType=(TextView)itemView.findViewById(R.id.txt_sportsType);
        }
    }
}
