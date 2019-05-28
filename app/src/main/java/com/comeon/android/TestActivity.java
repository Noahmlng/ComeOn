package com.comeon.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.HttpUtil;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends Activity_Parent {
    private static final String TAG = "TestActivity";

    TextView txt_test;
    Button btn_sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        txt_test = (TextView) findViewById(R.id.txt_test);
        btn_sendRequest = (Button) findViewById(R.id.btn_sendRequest);

        btn_sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest("https://github.com/Noahmlng/ComeOn/blob/master/app/src/main/assets/customConfigDir/custom_map_config.json", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.message();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_test.setText(responseText);
                            }
                        });
                    }
                });
            }
        });

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.mipmap.ic_plus);

        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton floatingActionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder itemBuidler1 = new SubActionButton.Builder(this);
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.mipmap.ic_plus);
        SubActionButton button1 = itemBuidler1.setContentView(itemIcon1).build();


        SubActionButton.Builder itemBuidler2 = new SubActionButton.Builder(this);
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.mipmap.ic_plus);
        SubActionButton button2 = itemBuidler2.setContentView(itemIcon2).build();

        SubActionButton.Builder itemBuidler3 = new SubActionButton.Builder(this);
        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.mipmap.ic_plus);
        SubActionButton button3 = itemBuidler3.setContentView(itemIcon3).build();

        FloatingActionMenu actionMenu=new FloatingActionMenu.Builder(this).addSubActionView(button1).addSubActionView(button2).addSubActionView(button3).attachTo(floatingActionButton).build();

    }
}
