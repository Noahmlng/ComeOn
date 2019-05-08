package com.comeon.android.service;

/**
 * 加载状态监听器
 */
public interface LoadingListener {

    void onProgress(int progress);

    void onSuccess();

    void onFail();

    void onPause();

    void onCancel();

}
