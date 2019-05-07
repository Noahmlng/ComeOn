package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comeon.android.R;

/**
 * 组团碎片
 */
public class GroupFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        return view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group;
    }

}
