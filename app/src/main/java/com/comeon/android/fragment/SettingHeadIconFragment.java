package com.comeon.android.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.comeon.android.R;
import com.comeon.android.RegistrationActivity;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 注册：设置头像碎片
 */
public class SettingHeadIconFragment extends Fragment implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;
    public static final int PICK_PHOTO = 2;
    private static final String TAG = "SettingHeadIconFragment";
    public Button btn_takePhoto;
    public Button btn_choosePhotoFromAlbum;
    public CircleImageView head_icon;
    private Uri newImageUri;

    public Uri getNewImageUri() {
        return newImageUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_headicon_fragment, container, false);
        initControls(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initControls(View view) {
        btn_choosePhotoFromAlbum = (Button) view.findViewById(R.id.btn_choosePhotoFromAlbum);
        btn_takePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
        head_icon = (CircleImageView) view.findViewById(R.id.head_icon);

        btn_choosePhotoFromAlbum.setOnClickListener(this);
        btn_takePhoto.setOnClickListener(this);
        head_icon.setOnClickListener(this);
    }


    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choosePhotoFromAlbum:
                /*
                    打开相册
                 */
                requestOpenAlbumPermission();
                break;
            case R.id.btn_takePhoto:
                /*
                    触发照相机
                 */
                requestCameraPermission();
                break;
            case R.id.head_icon:
                /*
                 */
                break;
        }
    }

    /**
     * 触发照相机
     *
     * @param phone 手机号码——通过user+（手机号码）+headIcon.png命名
     */
    private void takePhoto(String phone) {
        //1、创建保存拍照获取的图片
        File newImg = new File(getActivity().getExternalCacheDir(), "user" + phone + "headIcon.png");

        //2、判断是否存在，如果存在则删除原照片
        try {
            if (newImg.exists()) {
                newImg.delete();
            }
            newImg.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //3、获取储存位置的uri（Android 7.0之后协议的机制变化）
        if (Build.VERSION.SDK_INT >= 24) {
            newImageUri = FileProvider.getUriForFile(this.getActivity(), "com.comeon.android.fileprovider", newImg);
        } else {
            newImageUri = Uri.fromFile(newImg);
        }

        //4、设置隐式Intent，调用照相机应用程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, newImageUri);
        this.getActivity().startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 打开相册选择照片的操作
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        this.getActivity().startActivityForResult(intent, PICK_PHOTO);
    }

    /**
     * 针对Android 4.4系统以上的处理图片方法
     * 因为4.4版本后选取相册中的图片不再返回图片真实的Uri了，而是一个封装过的Uri，所以要进行额外的解析
     *
     * @param data
     */
    @TargetApi(19)
    public String handleImageOnKitKat(Intent data) {
        //1、定义变量储存待选择图片路径
        String imagePath = null;

        //获取选取图片封装后的uri
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this.getActivity(), uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //获取选择的图片id
                String id = docId.split(":")[1];
                //编写查询的条件
                String selection = MediaStore.Images.Media._ID + "=" + id;
                //调用获取图片真实路径的方法
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取路径即可
            imagePath = uri.getPath();
        }
        LogUtil.d(TAG, "handleImageOnKitKat: " + imagePath);
        return imagePath;
    }

    /**
     * 针对Android 4.4版本以下的图片处理方式
     *
     * @param data
     */
    public String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        LogUtil.d(TAG, "handleImageOnKitKat: " + imagePath);
        return imagePath;
    }

    /**
     * 根据选择的图片返回相应的图片路径
     *
     * @param uri       图片的Uri
     * @param selection 查询条件
     * @return 图片真实的路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取图片的真实路径
        Cursor cursor = this.getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 请求照相机权限，然后打开照相机
     */
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO);
        } else {
            takePhoto(((RegistrationActivity) getActivity()).getNewUser().getUserPhone());
        }
    }

    /**
     * 请求访问SD卡的权限，然后打开相册
     */
    private void requestOpenAlbumPermission() {
        //获取访问SD卡的权限
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else {
            openAlbum();
        }
    }

    /**
     * 处理请求权限返回的结果处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (permissions.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this.getActivity(), "没有照相机权限将无法进行拍照", Toast.LENGTH_SHORT).show();
                    } else {
                        takePhoto(((RegistrationActivity) getActivity()).getNewUser().getUserPhone());
                    }
                }
                break;

            case PICK_PHOTO:
                if (permissions.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this.getActivity(), "没有写入权限将无法打开相册", Toast.LENGTH_SHORT).show();
                    } else {
                        openAlbum();
                    }
                }
                break;
        }
    }


}
