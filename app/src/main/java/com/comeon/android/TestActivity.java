package com.comeon.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comeon.android.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    TextView txt_test;
    Button btn_sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        txt_test=(TextView)findViewById(R.id.txt_test);
        btn_sendRequest=(Button)findViewById(R.id.btn_sendRequest);

        btn_sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest("https://github.com/Noahmlng/ComeOn/blob/master/app/src/main/assets/customConfigDir/custom_map_config.json", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText=response.message();
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
    }
}
