package com.comeon.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 处理Http请求的工具类
 */
public class HttpUtil {

    /**
     * 使用okHttp开源库发送Http请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        //okhttp的神奇之处：enqueue的方法已经帮我们开好了子线程，并在子线程中执行http请求，最终将返回的结果回调到okhttp3.Callback中
        client.newCall(request).enqueue(callback);
    }

}
