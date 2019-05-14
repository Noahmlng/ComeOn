package com.comeon.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.adapter.MessageAdapter;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.Message;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;
import com.comeon.android.util.ViewUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity_Parent implements View.OnClickListener{
    private static final String TAG = "ChatActivity";

    GradientTextButton txt_friendName;
    ImageButton btn_goback;
    ImageButton btn_speak;
    ImageButton btn_emoji;
    EditText editText_messageContent;

    RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<com.comeon.android.db.Message> messages;
    private long friendId;

    UserBusinessInterface userBusiness=new UserBusiness();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setStatusBarColor(this, Color.WHITE, false);

        //接收传入数据
        friendId=getIntent().getLongExtra("friendId", 0);

        setContentView(R.layout.activity_chat);
        initData();
        initControls();
    }

    private void initControls(){
        txt_friendName=(GradientTextButton)findViewById(R.id.txt_friendName);
        txt_friendName.setText(LitePal.find(UserInfo.class,friendId).getUserNickName());

        editText_messageContent=(EditText)findViewById(R.id.txt_sendContent);
        editText_messageContent.setBackgroundResource(R.drawable.search_edittext);

        btn_goback=(ImageButton)findViewById(R.id.btn_goback);
        btn_speak=(ImageButton)findViewById(R.id.btn_speak);
        btn_emoji=(ImageButton)findViewById(R.id.btn_emoji);

        btn_goback.setOnClickListener(this);
        btn_speak.setOnClickListener(this);
        //暂时作为发送按钮
        btn_emoji.setOnClickListener(this);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter=new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);
    }

    private void initData(){
        messages=userBusiness.loadAllMessages(loginUser.getId(), friendId);
        LogUtil.d(TAG, "有"+messages.size()+"条消息记录");
    }

    /**
     * 打开的对外接口
     * @param context
     * @param friendId  打开...friend的聊天记录
     */
    public static void enterChatPage(Context context, long friendId){
        Intent intent=new Intent(context, ChatActivity.class);
        intent.putExtra("friendId",friendId);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_goback:
                finish();
                break;
            case R.id.btn_speak:
                Toast.makeText(this, "暂时不支持语音功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_emoji:
                String content=editText_messageContent.getText().toString().trim();
                //暂时做发送操作
                if(content.length()>0){
                    if(messages==null){
                        messages=new ArrayList<Message>();
                    }
                    Message sendMessage=userBusiness.sendMessage(loginUser.getId(), friendId, content);
                    messages.add(sendMessage);
                    // 新消息（自带的方法检测新对象的产生）
                    messageAdapter.notifyItemChanged(messages.size()-1);
                    //滚到最新位置
                    recyclerView.scrollToPosition(messages.size()-1);
                    //清空信息
                    editText_messageContent.setText("");
                }
                break;
        }
    }
}
