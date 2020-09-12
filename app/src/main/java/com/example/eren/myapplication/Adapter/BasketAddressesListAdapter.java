package com.example.eren.myapplication.Adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eren.myapplication.Models.AddressDetail;
import com.example.eren.myapplication.Models.Addresses;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BasketAddressesListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Addresses> mAddresses=new ArrayList<>();
    private Map<String,City> mCities=new TreeMap<>();
    public int mSelectedItem=0;
    Map<String,AddressDetail> addressDetailMap=new TreeMap<>();

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public BasketAddressesListAdapter(Context mContext, ArrayList<Addresses> mAddresses, Map<String,City> mCities,Map<String,AddressDetail> addressDetailMap) {
        this.mContext = mContext;
        this.mAddresses = mAddresses;
        this.mCities=mCities;
        this.addressDetailMap=addressDetailMap;
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
        View v = View.inflate(mContext, R.layout.basket_address_listviewitem,null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_name);
        TextView tvAddress = (TextView)v.findViewById(R.id.tv_address);
        TextView txt=v.findViewById(R.id.tv_city);
        RelativeLayout relativeLayout=v.findViewById(R.id.basket_list_items);
        TextView tvadresid=v.findViewById(R.id.adresId);

        if(mCities.get(mAddresses.get(position).getId())!=null)
        txt.setText(mCities.get(mAddresses.get(position).getId()).getName());
        tvName.setText(mAddresses.get(position).getName());
        tvAddress.setText(mAddresses.get(position).getAddress());
        tvadresid.setText(addressDetailMap.get(mAddresses.get(position).getAddressId()).getNeighborhood());


        if (position == mSelectedItem) {

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.background_products) );
            } else {
                relativeLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_products));
            }
        }


        v.setTag(mAddresses.get(position).getAddressId());
        return v;
    }
}
