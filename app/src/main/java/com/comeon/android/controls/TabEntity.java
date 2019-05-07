package com.comeon.android.controls;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * 自定义下滑块的实体类
 */
public class TabEntity implements CustomTabEntity {

    public String title;
    private int selectedIcon;
    private int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

}
