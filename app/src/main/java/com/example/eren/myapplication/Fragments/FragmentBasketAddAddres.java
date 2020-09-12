package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.Models.District;
import com.example.eren.myapplication.Models.Neighborhood;
import com.example.eren.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBasketAddAddres extends Fragment implements IOnBackPressed {



    private EditText et_addressName, et_description;
    private Spinner spn_cities,spn_districts,spn_neighborhoods;
    private Button btn_addNewAddress;

    private static final String TAG = FragmentAddNewAddress.class.getSimpleName();
    private RequestQueue mQueue;

    private ArrayList<City> cities;
    private ArrayList<District> districts,districtsArray;
    private ArrayList<Neighborhood> neighborhoods,neighborhoodsArray;

    private String selectCity="";
    private String selectDistrict="";
    private String selectNeighborhood="";
    SharedPreferences sharedPreferences35;


    public FragmentBasketAddAddres() {
        // Required empty public constructor
    }
    public static FragmentBasketAddAddres newInstance(String param1) {
        FragmentBasketAddAddres fragment = new FragmentBasketAddAddres();
        //   Bundle args = new Bundle();
        // args.putString(KEY_TITLE, param1);
        //fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_add_new_address, container, false);

        sharedPreferences35= getContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);
        et_addressName=view.findViewById(R.id.et_addressName_addnewAddress);
        et_description = view.findViewById(R.id.et_description_addnewAddress);
        spn_cities = view.findViewById(R.id.spn_cities_addnewAddress);
        spn_districts = view.findViewById(R.id.spn_districts_addnewAddress);
        spn_neighborhoods = view.findViewById(R.id.spn_neighborhoods_addnewAddress);
        btn_addNewAddress=view.findViewById(R.id.btn_addnewAddress_submit);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCities();

        spn_cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) adapterView.getSelectedItem();
                String code = city.getCode();
                selectCity=city.getId().toString();
                getDistricts(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn_districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                District district = (District) adapterView.getSelectedItem();
                String id = district.getId();
                selectDistrict=district.getId().toString();
                getNeighborhoods(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn_neighborhoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Neighborhood neighborhood = (Neighborhood) adapterView.getSelectedItem();
                String id = neighborhood.getId();
                selectNeighborhood=neighborhood.getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( et_addressName.getText().toString().trim().equals("") || et_description.getText().toString().trim().equals("") || spn_cities.getCount()==0 || spn_districts.getCount()==0 || spn_neighborhoods.getCount()==0)
                {
                    Toast.makeText(getContext(), "Lütfen Gerekli Alanları Doldurunuz", Toast.LENGTH_SHORT).show();

                } else {
                    postAddNewAddress(selectCity,selectDistrict,selectNeighborhood,et_description.getText().toString());

                    NavigationManager navigationManager= MainActivity.navigationManager;
                    navigationManager.showFragment(FragmentAddresses.newInstance(""),false);
                }

            }
        });

    }
    private void getCities(){
        String url = "http://10.0.3.2:3000/cities";
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cities= new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("city");
                            City firstElement=new City("0","Lütfen Seçim Yapınız","0");
                            cities.add(firstElement);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject city = jsonArray.getJSONObject(i);
                                String id = city.getString("_id");
                                String name = city.getString("name");
                                String code = city.getString("code");
                                City p=new City(id,name,code);
                                cities.add(p);
                            }
                            refreshData_getCities();
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

    private void refreshData_getCities() {
        ArrayAdapter<City> adapter;
        if (cities.isEmpty()) {
            Log.i(TAG, "Veri Yok");
        } else {
            adapter = new ArrayAdapter<City>(getContext(), android.R.layout.simple_spinner_item, cities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_cities.setAdapter(adapter);
        }
    }

    public void getDistricts(final String cityCode){
        String url = "http://10.0.3.2:3000/districts";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        districts= new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("District");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject district = jsonArray.getJSONObject(i);
                                String id = district.getString("_id");
                                String name = district.getString("name");
                                String code = district.getString("code");
                                District p=new District(id,name,code);
                                districts.add(p);
                            }
                            refreshData_getDistricts(cityCode);

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

    public void refreshData_getDistricts(String cityCode) {
        districtsArray= new ArrayList<>();
        ArrayAdapter<District> adapter;
        if (districts.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {
            for (int i = 0; i < districts.size(); i++) {
                if(cityCode.equals(districts.get(i).getCode())){
                    District p=new District(districts.get(i).getId(),districts.get(i).getName(),districts.get(i).getCode());
                    districtsArray.add(p);
                }
            }
            adapter = new ArrayAdapter<District>(getContext(),android.R.layout.simple_list_item_1,districtsArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_districts.setAdapter(adapter);
        }
    }

    public void getNeighborhoods(final String districtId){
        String url = "http://10.0.3.2:3000/neighborhoods";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        neighborhoods= new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("Neighborhood");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject neighborhood = jsonArray.getJSONObject(i);
                                String id = neighborhood.getString("_id");
                                String name = neighborhood.getString("name");
                                String district = neighborhood.getString("District");
                                Neighborhood p=new Neighborhood(id,name,district);
                                neighborhoods.add(p);
                            }
                            refreshData_getNeighborhoods(districtId);
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

    public void refreshData_getNeighborhoods(String districtId) {
        ArrayAdapter<Neighborhood> adapter;
        neighborhoodsArray= new ArrayList<>();
        if (neighborhoods.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {
            for (int i = 0; i < neighborhoods.size(); i++) {
                if(districtId.equals(neighborhoods.get(i).getDistrict())){
                    Neighborhood p=new Neighborhood(neighborhoods.get(i).getId(),neighborhoods.get(i).getName(),neighborhoods.get(i).getDistrict());
                    neighborhoodsArray.add(p);
                }
            }
            adapter = new ArrayAdapter<Neighborhood>(getContext(),android.R.layout.simple_list_item_1,neighborhoodsArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_neighborhoods.setAdapter(adapter);
        }
    }

    private void postAddNewAddress(final String city, final String district,final String neighborhood, final String description)
    {
        String url = "http://10.0.3.2:3000/addresses";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    Log.i("ouz","----------------------"+description);
                    obj = new JSONObject(response);
                    JSONObject neighborhood = obj.getJSONObject("createdAddress");
                    String addressId = neighborhood.getString("_id");
                    String userIDlogin=sharedPreferences35.getString("userid","");
                    postAddNewAddressName(addressId,et_addressName.getText().toString(),userIDlogin);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Kayıt Eklenemedi", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("city", city); //Add the data you'd like to send to the server.
                MyData.put("district", district);
                MyData.put("neighborhood", neighborhood);
                MyData.put("description", description);

                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }
    private void postAddNewAddressName(final String addressId,final String addressName, final String userId)
    {
        String url = "http://10.0.3.2:3000/userAddresses";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    JSONObject neighborhood = obj.getJSONObject("createdAddress");
                    String addressId = neighborhood.getString("_id");
                    Toast.makeText(getContext(), "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Kayıt Eklenemedi", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("user", userId); //Add the data you'd like to send to the server.
                MyData.put("address", addressId);
                MyData.put("name", addressName);

                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }
    public boolean onBackPressed() {
        NavigationManager navigationManager=MainActivity.navigationManager;
        navigationManager.showFragment(FragmentAddresses.newInstance(""),false);

        return true;
    }

}
