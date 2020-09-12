package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.AddressesListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAddresses extends Fragment implements IOnBackPressed {

    public static final String TAG = Addresses.class.getSimpleName();
    public RequestQueue mQueue,mQueue2;
    public ArrayList<Addresses> addresses;
    public ArrayList<AddressDetail> addressDetail;
    public ArrayList<Addresses> mAddresses=new ArrayList<>();
    private AddressesListAdapter adapter;
    public ListView lvAddresses;
    private Button btn_AddNewAddress;
    public Map<String,Addresses> mapAdresses;

    SharedPreferences sharedPreferences35;
    public Map<String,City> cities=new TreeMap<>();
    public MyAddresses() {
        // Required empty public constructor
    }

    public static MyAddresses newInstance(String param1) {
        MyAddresses fragment = new MyAddresses();

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_addresses, container, false);

        lvAddresses = (ListView)view.findViewById(R.id.lv_addresses);
        btn_AddNewAddress = (Button) view.findViewById(R.id.btn_addnewAddress);

        sharedPreferences35= getContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        String userIDlogin=sharedPreferences35.getString("userid","");
        getUserAddresses(userIDlogin);
        lvAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.notifyDataSetChanged();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("addressId",view.getTag(R.id.getAddressId).toString());
                editor.putString("addressName",view.getTag(R.id.getAdressName).toString());
                editor.putString("userAddressId",view.getTag(R.id.getUserAddressId).toString());
                editor.putString("getAddressDesc",view.getTag(R.id.getAddressDesc).toString());

                editor.commit();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddressDetailUpdate fragment = new AddressDetailUpdate();
                fm.beginTransaction().replace(R.id.container,fragment).commit();
            }
        });
        btn_AddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationManager=MainActivity.navigationManager;
                navigationManager.showFragment(FragmentAddNewAddress.newInstance(""),false);
            }
        });

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

                            adapter = new AddressesListAdapter(getContext(),mAddresses,cities);
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


                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            Gson gson = new Gson();
                            String json = gson.toJson(p);
                            editor.putString(addressId, json);
                            editor.commit();

                            Addresses olr=mapAdresses.get(addressId);
                            mAddresses.add(new Addresses(olr.getId(),olr.getName(),olr.getUser(),description,olr.getAddressId()));

                            fonksiyon(city,olr.getId());

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

    void fonksiyon(String cityid, final String addressId){

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

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }


    public NavigationManager navigationManager;
    @Override
    public boolean onBackPressed() {
        navigationManager= MainActivity.navigationManager;

        navigationManager.showFragment(FragmentSettings.newInstance(""),false);

        return true;
    }

}
