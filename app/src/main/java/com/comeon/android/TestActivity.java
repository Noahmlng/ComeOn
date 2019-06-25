package com.comeon.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.HttpUtil;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.litepal.LitePal;

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

        Toast.makeText(this, "当前有"+LitePal.count(AppointmentOrder.class) +"订单",Toast.LENGTH_SHORT).show();
        OrderBusiness orderBusiness=new OrderBusiness();
        Toast.makeText(this, "最后一个订单的发起者电话："+orderBusiness.getAllOrders().get(4).getOrderSponsor().getUserPhone(),Toast.LENGTH_SHORT).show();
    }
}
