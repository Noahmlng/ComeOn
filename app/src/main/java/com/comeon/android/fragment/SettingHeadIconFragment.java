package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comeon.android.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 注册：设置头像碎片
 */
public class SettingHeadIconFragment extends Fragment implements View.OnClickListener{

    Button btn_takePhoto;
    Button btn_choosePhotoFromAlbum;
    CircleImageView head_icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.setting_headicon_fragment, container, false);
        initControls(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initControls(View view){
        btn_choosePhotoFromAlbum=(Button)view.findViewById(R.id.btn_choosePhotoFromAlbum);
        btn_takePhoto=(Button)view.findViewById(R.id.btn_takePhoto);
        head_icon=(CircleImageView)view.findViewById(R.id.head_icon);

        btn_choosePhotoFromAlbum.setOnClickListener(this);
        btn_takePhoto.setOnClickListener(this);
        head_icon.setOnClickListener(this);
    }


    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_choosePhotoFromAlbum:
                break;
            case R.id.btn_takePhoto:
                break;
            case R.id.head_icon:
                break;
        }
    }
}
