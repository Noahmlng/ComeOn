package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.comeon.android.R;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.fragment.StadiumsFragment;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import java.util.List;

/**
 * 体育场馆信息适配器
 */
public class StadiumsAdapter extends RecyclerView.Adapter<StadiumsAdapter.ViewHolder> {

    private List<StadiumInfo> stadiums;

    /**
     * 创建构造方法——初始化场馆集合
     * @param stadiums  传入的场馆对象集合
     */
    public StadiumsAdapter(List<StadiumInfo> stadiums){
        this.stadiums=stadiums;
    }

    /**
     * 重写创建布局中控件缓存的方法
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.stadium_item, viewGroup, false);
        //获取子项布局中的控件
        ViewHolder viewHolder=new ViewHolder(view);
        /**
         * 处理子项点击事件——弹出场馆详情活动
         */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //....
            }
        });
        return viewHolder;
    }

    /**
     * 绑定子项中的控件的属性和事件
     * @param viewHolder  包裹子项的存储器
     * @param position  当前位置索引
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        StadiumInfo stadium=stadiums.get(position);
        viewHolder.txt_stadiumType.setText(stadium.getStadium_type().getType_name());
        viewHolder.txt_stadiumDistrict.setText(stadium.getLocation().getDistrict());
        viewHolder.txt_stadiumName.setText(stadium.getStadium_name());
    }

    @Override
    public int getItemCount() {
        return stadiums.size();
    }

    /**
     * 匿名内部类包裹缓存中的控件
     */
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
