package com.example.eren.myapplication.Interface;

import android.support.v4.app.Fragment;

import com.example.eren.myapplication.Fragments.FragmentProducts;

public interface NavigationManager {
    void showFragment(String title);
    void showFragment(Fragment fragment, boolean allowStateLoss);
}
