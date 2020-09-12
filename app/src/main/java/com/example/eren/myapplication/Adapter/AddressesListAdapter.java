package com.example.eren.myapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eren.myapplication.Models.Addresses;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AddressesListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Addresses> mAddresses=new ArrayList<>();
    private Map<String,City> mCities=new TreeMap<>();
    public int mSelectedItem=0;

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public AddressesListAdapter(Context mContext, ArrayList<Addresses> mAddresses, Map<String,City> mCities) {
        this.mContext = mContext;
        this.mAddresses = mAddresses;
        this.mCities=mCities;
    }

    @Override
    public int getCount() {
        int count=mCities.size();
        if(mAddresses.size()<mCities.size())
            count=mAddresses.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mAddresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.listviewitem,null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_name);
        TextView tvAddress = (TextView)v.findViewById(R.id.tv_address);
        TextView txt=v.findViewById(R.id.tv_city);

        if(mCities.get(mAddresses.get(position).getId())!=null)
            txt.setText(mCities.get(mAddresses.get(position).getId()).getName());
        tvName.setText(mAddresses.get(position).getName());
        tvAddress.setText(mAddresses.get(position).getAddress());

        v.setTag(R.id.getAddressDesc,mAddresses.get(position).getAddress());
        v.setTag(R.id.getAddressId,mAddresses.get(position).getAddressId());
        v.setTag(R.id.getAdressName,mAddresses.get(position).getName());
        v.setTag(R.id.getUserAddressId,mAddresses.get(position).getId());
        return v;
    }
}
