package com.example.eren.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.eren.myapplication.Fragments.FragmentMarketsProducts;
import com.example.eren.myapplication.Fragments.FragmentSettings;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.ShopUnit;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class MarketsFragmentPagerAdapter extends FragmentStatePagerAdapter {


    private ArrayList<String> mNames=new ArrayList<>();
    private ArrayList<String> mCategoryId=new ArrayList<>();
    Context context;
    ArrayList<ShopUnit> shops=new ArrayList<>();
    Map<String,ShopUnit> shopMap=new TreeMap<>();


    public MarketsFragmentPagerAdapter(FragmentManager fm, Context context,ArrayList<ShopUnit> shops,Map<String,ShopUnit> shopMap) {
        super(fm);
        this.context=context;
        this.shops=shops;
        this.shopMap=shopMap;


    }

    @Override
    public Fragment getItem(int i) {

        FragmentMarketsProducts fragmentSettings=new FragmentMarketsProducts().setShops(shops.get(i)).shopMap(shopMap).setPos(i);

        return fragmentSettings;
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return shops.get(position).getName();
    }

    public void clearAdapterData() {
        shops.clear();
        shopMap.clear();
        notifyDataSetChanged();
    }

}