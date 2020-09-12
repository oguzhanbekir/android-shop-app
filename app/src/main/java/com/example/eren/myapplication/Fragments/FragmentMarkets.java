package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.MarketsFragmentPagerAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.Products;
import com.example.eren.myapplication.Models.ShopUnit;
import com.example.eren.myapplication.Models.ShopUnitServiceAddress;
import com.example.eren.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMarkets extends Fragment implements IOnBackPressed {


    private static final String NEIGHBOORHOOD="mahalleId";
    private String neighboorhood;
    private NavigationManager navigationManager;
    ViewPager viewPager;
    TabLayout tabLayout;
    MarketsFragmentPagerAdapter adapter;
    SharedPreferences sharedPreferences2;

    public FragmentMarkets() {
        // Required empty public constructor
    }
    public static FragmentMarkets newInstance(String neighboorhood) {
        FragmentMarkets fragment = new FragmentMarkets();
       // Bundle args = new Bundle();
       // args.putString(NEIGHBOORHOOD, neighboorhood);
      //  fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationManager= MainActivity.navigationManager;

        sharedPreferences2= getActivity().getSharedPreferences("marketadres", Context.MODE_PRIVATE);

        neighboorhood=sharedPreferences2.getString("selectedAddress","");

        View view = inflater.inflate(R.layout.fragment_fragment_markets, container, false);
        viewPager=view.findViewById(R.id.viewpager_home);
        adapter=new MarketsFragmentPagerAdapter(getFragmentManager(),inflater.getContext(),shops,shopMap);
        viewPager.setAdapter(adapter);
        setMarkets();

        tabLayout=view.findViewById(R.id.tab_layout_home);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(onTabSelectedListener(viewPager));

        // Inflate the layout for this fragment
        return view;
    }


    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }

    public static final String TAG = Products.class.getSimpleName();
    public RequestQueue mQueue;
    public ShopUnitServiceAddress shopUnitServiceAddress;
    public ArrayList<ShopUnitServiceAddress> marketsSet=new ArrayList<>();
    private ArrayList<ShopUnitServiceAddress> shopUnitServiceAddresses;

    public void setMarkets(){
        String url = "http://10.0.3.2:3000/shopUnitServiceAddresses?neighborhood="+neighboorhood;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("ShopUnitServiceAddress");
                            shopUnitServiceAddresses=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject product = jsonArray.getJSONObject(i);
                                String id = product.getString("_id");
                                String shopUnit= product.getString("shopUnit");
                                ShopUnitServiceAddress p=new ShopUnitServiceAddress(id,shopUnit);
                                shopUnitServiceAddresses.add(p);
                                setMarketDetails(p.getShopUnit());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }



    ArrayList<ShopUnit> shops=new ArrayList<>();
    Map<String,ShopUnit> shopMap=new TreeMap<>();
    public void setMarketDetails(String shopID){
        String id=shopID;
        String url = "http://10.0.3.2:3000/shopUnits/"+id;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonAddressesDetail = response.getJSONObject("shopUnit");
                            String id = jsonAddressesDetail.getString("_id");
                            String name = jsonAddressesDetail.getString("name");
                            String shop = jsonAddressesDetail.getString("shop");
                            Integer min_order = jsonAddressesDetail.getInt("min_order");
                            String phone = jsonAddressesDetail.getString("phone");
                            String email = jsonAddressesDetail.getString("email");
                            Boolean status = jsonAddressesDetail.getBoolean("status");

                            ShopUnit p=new ShopUnit(id,name,shop,min_order,phone,email,status);
                            shops.add(p);

                            shopMap.put(id,p);

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
    @Override
    public boolean onBackPressed() {
        navigationManager.showFragment(FragmentBasket.newInstance(""),false);

        return true;
    }
}
