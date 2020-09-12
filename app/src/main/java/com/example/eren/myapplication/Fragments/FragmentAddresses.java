package com.example.eren.myapplication.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.BasketAddressesListAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.AddressDetail;
import com.example.eren.myapplication.Models.Addresses;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;


public class FragmentAddresses extends Fragment implements IOnBackPressed {
    public static final String TAG = Addresses.class.getSimpleName();
    public RequestQueue mQueue,mQueue2;
    public ArrayList<Addresses> addresses;
    public ArrayList<AddressDetail> addressDetail;
    public ArrayList<Addresses> mAddresses=new ArrayList<>();
    private BasketAddressesListAdapter adapter;
    public ListView lvAddresses;
    private Button btn_showmarkets;
    private Button btn_addadres;
    private NavigationManager navigationManager;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences35;

    int mSelectedItem;

    public Map<String,Addresses> mapAdresses;

    public Map<String,City> cities=new TreeMap<>();
    public FragmentAddresses() {
        // Required empty public constructor
    }

    public static FragmentAddresses newInstance(String param1) {
        FragmentAddresses fragment = new FragmentAddresses();
        //   Bundle args = new Bundle();
        // args.putString(KEY_TITLE, param1);
        //fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_addresses, container, false);
        sharedPreferences2= getActivity().getSharedPreferences("marketadres", Context.MODE_PRIVATE);
        navigationManager= MainActivity.navigationManager;

        sharedPreferences35= getContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);
        lvAddresses = (ListView)view.findViewById(R.id.lv_addresses);
        btn_showmarkets=view.findViewById(R.id.button_show_markets);
        btn_addadres=view.findViewById(R.id.basket_button_add_address);
        adapter = new BasketAddressesListAdapter(getContext(),mAddresses,cities,addressDetailMap);
        lvAddresses.setAdapter(adapter);


        btn_showmarkets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationManager.showFragment(FragmentMarkets.newInstance(""),false);
            }
        });

        btn_addadres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationManager.showFragment(FragmentBasketAddAddres.newInstance(""),false);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        lvAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getContext(),"id"+view.getTag(),Toast.LENGTH_SHORT).show();
                adapter.setmSelectedItem(i);
                adapter.notifyDataSetChanged();

                TextView txt=view.findViewById(R.id.adresId);
                String aaaa= String.valueOf(txt.getText());

                SharedPreferences.Editor prefEditor;
                prefEditor = sharedPreferences2.edit();
                prefEditor.putString("selectedAddress",aaaa);
                prefEditor.commit();

            }
        });

        String userIDlogin=sharedPreferences35.getString("userid","");
        getUserAddresses(userIDlogin);

    }

    public void getUserAddresses(String userId){
        String url = "http://10.0.3.2:3000/UserAddresses?user="+userId;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        addresses = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("UserAddresses");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonAddresses = jsonArray.getJSONObject(i);
                                String id = jsonAddresses.getString("_id");
                                String name = jsonAddresses.getString("name");
                                String user = jsonAddresses.getString("user");
                                String address = jsonAddresses.getString("address");
                                Addresses p=new Addresses(id,name,user,address,address);
                                addresses.add(p);
                            }
                            refreshData();

                            adapter = new BasketAddressesListAdapter(getContext(),mAddresses,cities,addressDetailMap);
                            lvAddresses.setAdapter(adapter);

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



    public void refreshData() {
        mAddresses = new ArrayList<>();
        addressDetail = new ArrayList<>();
        mapAdresses=new TreeMap<>();

        if (addresses.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {

            for (int i = 0; i < addresses.size(); i++)
                mapAdresses.put(addresses.get(i).getAddress(),addresses.get(i));
            for (int i = 0; i < addresses.size(); i++) {
                getUserAddressesDetail(addresses.get(i).getAddress());
            }
        }


    }

    Map<String,AddressDetail> addressDetailMap=new TreeMap<>();
    void getUserAddressesDetail(final String addressId){
        String url = "http://10.0.3.2:3000/addresses/"+addressId;

        mQueue2 = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonAddressesDetail = response.getJSONObject("address");
                            String _id = jsonAddressesDetail.getString("_id");
                            String city = jsonAddressesDetail.getString("city");
                            String district = jsonAddressesDetail.getString("district");
                            String neighborhood = jsonAddressesDetail.getString("neighborhood");
                            String description = jsonAddressesDetail.getString("description");
                            AddressDetail p=new AddressDetail(_id,city,district,neighborhood,description);
                            addressDetail.add(p);
                            Addresses olr=mapAdresses.get(addressId);

                            addressDetailMap.put(addressId,p);
                            mAddresses.add(new Addresses(olr.getId(),olr.getName(),olr.getUser(),description,olr.getAddressId()));


                            adapter.notifyDataSetChanged();
                            fonksiyon(city,olr.getId(),neighborhood);

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

    void fonksiyon(String cityid, final String addressId, final String neighborhood){

        String url = "http://10.0.3.2:3000/cities/"+cityid;
        mQueue2 = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonAddressesDetail = response.getJSONObject("city");
                            String id = jsonAddressesDetail.getString("_id");
                            String name = jsonAddressesDetail.getString("name");
                            String code = jsonAddressesDetail.getString("code");
                            City p=new City(id,name,code);

                            cities.put(addressId,p);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();

                        SharedPreferences.Editor prefEditor;
                        prefEditor = sharedPreferences2.edit();
                        prefEditor.putString("selectedAddress",addressDetail.get(0).getNeighborhood());
                        prefEditor.commit();

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