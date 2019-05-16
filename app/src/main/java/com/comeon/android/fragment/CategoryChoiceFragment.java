package com.comeon.android.fragment;

import android.view.View;
import android.widget.ImageButton;

import com.comeon.android.LaunchOrderActivity;
import com.comeon.android.R;
import com.comeon.android.util.LogUtil;

/**
 * 选择运动种类的碎片
 */
public class CategoryChoiceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "CategoryChoiceFragment";

    ImageButton btn_categorySports;
    ImageButton btn_categoryExercise;
    ImageButton btn_categoryAerobic;
    ImageButton btn_categoryAnaerobic;
    ImageButton btn_categoryEntertainment;
    ImageButton btn_categoryRelax;
    private LaunchOrderFragment launchOrderFragment;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initControls(View view) {
        launchOrderFragment = (LaunchOrderFragment)getParentFragment();
        btn_categoryAerobic = (ImageButton) view.findViewById(R.id.btn_categoryAerobic);
        btn_categorySports = (ImageButton) view.findViewById(R.id.btn_categorySports);
        btn_categoryExercise = (ImageButton) view.findViewById(R.id.btn_categoryExercise);
        btn_categoryAnaerobic = (ImageButton) view.findViewById(R.id.btn_categoryAnaerobic);
        btn_categoryEntertainment = (ImageButton) view.findViewById(R.id.btn_categoryEntertainment);
        btn_categoryRelax = (ImageButton) view.findViewById(R.id.btn_categoryRelax);

        btn_categoryAerobic.setOnClickListener(this);
        btn_categorySports.setOnClickListener(this);
        btn_categoryExercise.setOnClickListener(this);
        btn_categoryAnaerobic.setOnClickListener(this);
        btn_categoryEntertainment.setOnClickListener(this);
        btn_categoryRelax.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_categoryAerobic:
                launchOrderFragment.addFragment(3);
                break;
            case R.id.btn_categorySports:
                launchOrderFragment.addFragment(1);
                break;
            case R.id.btn_categoryExercise:
                launchOrderFragment.addFragment(2);
                break;
            case R.id.btn_categoryAnaerobic:
                launchOrderFragment.addFragment(4);
                break;
            case R.id.btn_categoryEntertainment:
                launchOrderFragment.addFragment(5);
                break;
            case R.id.btn_categoryRelax:
                launchOrderFragment.addFragment(6);
                break;
        }
    }
}
