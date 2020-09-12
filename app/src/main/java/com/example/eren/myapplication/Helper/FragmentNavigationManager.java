package com.example.eren.myapplication.Helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.eren.myapplication.BuildConfig;
import com.example.eren.myapplication.Fragments.FragmentContent;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.R;

public class FragmentNavigationManager implements NavigationManager {

    private static FragmentNavigationManager mInstance;

    private FragmentManager mFragmentManager;
    private MainActivity mainActivity;

    public static FragmentNavigationManager getmInstance(MainActivity mainActivity){
        if (mInstance==null){
            mInstance=new FragmentNavigationManager();
        }
        mInstance.configure(mainActivity);

        return mInstance;
    }

    private void configure(MainActivity mainActivity){
        this.mainActivity=mainActivity;
        mFragmentManager=mainActivity.getSupportFragmentManager();
    }

    @Override
    public void showFragment(String title) {
        showFragment(FragmentContent.newInstance(title),false);
    }

    public void showFragment(Fragment fragment,boolean allowStateLoss){
        FragmentManager fm=mFragmentManager;
        FragmentTransaction ft=fm.beginTransaction().replace(R.id.container,fragment);
        ft.addToBackStack(null);

        if(allowStateLoss || !BuildConfig.DEBUG)
            ft.commitAllowingStateLoss();
        else
            ft.commit();
        fm.executePendingTransactions();

    }
}
