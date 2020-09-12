package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.AddressDetail;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.Models.District;
import com.example.eren.myapplication.Models.Neighborhood;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressDetailUpdate extends Fragment implements IOnBackPressed{
    public static final String TAG = AddressDetailUpdate.class.getSimpleName();
    public RequestQueue mQueue;

    private EditText et_description,et_addressName;
    private Button btn_updateAddress;
    private Spinner spn_cities,spn_districts,spn_neighborhoods;
    private  Button btn_delete;
    public ArrayList<City> cities;
    public ArrayList<District> districts,districtsArray;
    public ArrayList<Neighborhood> neighborhoods,neighborhoodsArray;

    private  String selectCity="";
    private  String selectDistrict="";
    private  String selectNeighborhood="";

    public AddressDetailUpdate() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_detail_update, container, false);

        et_description = view.findViewById(R.id.et_description);
        spn_cities = view.findViewById(R.id.spn_cities);
        spn_districts = view.findViewById(R.id.spn_districts);
        spn_neighborhoods = view.findViewById(R.id.spn_neighborhoods);
        btn_updateAddress=view.findViewById(R.id.btn_updateAdress);
        et_addressName=view.findViewById(R.id.et_addressName);
        btn_delete=view.findViewById(R.id.btn_deleteAddress);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String addressId = sharedPref.getString("addressId","Kayıt Yok");
        String addressName = sharedPref.getString("addressName","Kayıt Yok");
        String userAddressId = sharedPref.getString("userAddressId","Kayıt Yok");
        String userAddressDesc = sharedPref.getString("getAddressDesc","Kayıt Yok");



        et_description.setText(userAddressDesc);
        et_addressName.setText(addressName);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCities();

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("Emin misiniz?").setCancelable(false)
                        .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                String userid = sharedPref.getString("addressId","Kayıt Yok");
                                String userAddressId = sharedPref.getString("userAddressId","Kayıt Yok");
                                deleteAddress(userid);
                                deleteUserAddress(userAddressId);

                                MainActivity.navigationManager.showFragment(MyAddresses.newInstance(""),false);
                            }
                        })
                        .setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.setTitle("Seçilen adresi siliyorsunuz.");
                alertDialog.show();

            }
        });

        btn_updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( et_addressName.getText().toString().trim().equals("") || et_description.getText().toString().trim().equals("") || spn_cities.getCount()==0 || spn_districts.getCount()==0 || spn_neighborhoods.getCount()==0)
                {
                    Toast.makeText(getContext(), "Lütfen Gerekli Alanları Doldurunuz", Toast.LENGTH_SHORT).show();

                } else {
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    String id = sharedPref.getString("addressId","Kayıt Yok");
                    String userAddressId = sharedPref.getString("userAddressId","Kayıt Yok");


                    String addressName = et_addressName.getText().toString();
                    String addressDetail = et_description.getText().toString();

                    OkHttpClient clientAddress = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "[{\"propName\": \"description\",\"value\":\""+addressDetail+"\"},{\"propName\": \"city\",\"value\":\"" + selectCity + "\"},{\"propName\": \"district\",\"value\":\"" + selectDistrict + "\"},{\"propName\": \"neighborhood\",\"value\":\"" + selectNeighborhood + "\"}]");
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://10.0.3.2:3000/addresses/"+id)
                            .patch(body)
                            .addHeader("Content-Type", "application/json")
                            .build();

                    try {
                        okhttp3.Response response = clientAddress.newCall(request).execute();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    OkHttpClient clientUserAddress = new OkHttpClient();

                    MediaType mediaType2 = MediaType.parse("application/json");
                    RequestBody body2 = RequestBody.create(mediaType2, "[{\"propName\": \"name\",\"value\":\""+addressName+"\"}]");
                    okhttp3.Request request2 = new okhttp3.Request.Builder()
                            .url("http://10.0.3.2:3000/UserAddresses/"+userAddressId)
                            .patch(body2)
                            .addHeader("Content-Type", "application/json")
                            .build();

                    try {
                        okhttp3.Response response2 = clientUserAddress.newCall(request2).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Başarılı bir şekilde kaydedildi.", Toast.LENGTH_SHORT).show();
                    NavigationManager navigationManager= MainActivity.navigationManager;
                    navigationManager.showFragment(MyAddresses.newInstance(""),false);
                }
            }
        });
        spn_cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCity="";
                selectDistrict="";
                selectNeighborhood="";
                City city = (City) parent.getSelectedItem();
                String code = city.getCode();
                selectCity=city.getId().toString();
                getDistricts(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = (District) parent.getSelectedItem();
                String id_district = district.getId();
                selectDistrict=district.getId().toString();
                getNeighborhoods(id_district);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_neighborhoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Neighborhood neighborhood = (Neighborhood) parent.getSelectedItem();
                selectNeighborhood=neighborhood.getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getCities(){
        String url = "http://10.0.3.2:3000/cities";
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cities= new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("city");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject city = jsonArray.getJSONObject(i);
                                String id = city.getString("_id");
                                String name = city.getString("name");
                                String code = city.getString("code");
                                City p=new City(id,name,code);
                                cities.add(p);
                            }
                            refreshData();
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
        ArrayAdapter<City> adapter;
        String cityName="";
        if (cities.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {
            adapter = new ArrayAdapter<City>(getContext(),android.R.layout.simple_spinner_item,cities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_cities.setAdapter(adapter);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String addressId = sharedPref.getString("addressId", "");

            Gson gson = new Gson();
            String json = sharedPref.getString(addressId, "");
            AddressDetail obj = gson.fromJson(json, AddressDetail.class);

            for(int i=0;i<cities.size();i++){
                if(cities.get(i).getId().equals(obj.getCity())){
                    cityName=cities.get(i).getName().toString();
                }
            }
            for (int i=0;i<spn_cities.getCount();i++){
                if (spn_cities.getItemAtPosition(i).toString().equalsIgnoreCase(cityName)){
                    spn_cities.setSelection(i);
                }
            }
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
                            refreshData2(cityCode);
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

    public void refreshData2(String cityCode) {
        String districtName="";
        ArrayAdapter<District> adapter;
        districtsArray= new ArrayList<>();
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

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String addressId = sharedPref.getString("addressId", "");

            Gson gson = new Gson();
            String json = sharedPref.getString(addressId, "");
            AddressDetail obj = gson.fromJson(json, AddressDetail.class);


            for(int i=0;i<districts.size();i++){
                if(districts.get(i).getId().equals(obj.getDistrict().toString())){
                    districtName=districts.get(i).getName().toString();
                }
            }
            for (int i=0;i<spn_districts.getCount();i++){
                if (spn_districts.getItemAtPosition(i).toString().equalsIgnoreCase(districtName)){
                    spn_districts.setSelection(i);
                }
            }
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
                            refreshData3(districtId);
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

    public void refreshData3(String districtId) {
        String neighborhoodName="";
        SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);
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

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String addressId = sharedPref.getString("addressId", "");

            Gson gson = new Gson();
            String json = sharedPref.getString(addressId, "");
            AddressDetail obj = gson.fromJson(json, AddressDetail.class);

            for(int i=0;i<neighborhoods.size();i++){
                if(neighborhoods.get(i).getId().equals(obj.getNeighborhood().toString())){
                    neighborhoodName=neighborhoods.get(i).getName().toString();
                }
            }
            for (int i=0;i<spn_neighborhoods.getCount();i++){
                if (spn_neighborhoods.getItemAtPosition(i).toString().equalsIgnoreCase(neighborhoodName)){
                    spn_neighborhoods.setSelection(i);
                }
            }
        }
    }
    public void deleteAddress(String addressId) {
        String url = "http://10.0.3.2:3000/addresses/"+addressId;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "delete onResponse : " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d(TAG + ": ", "delete Error Response code: " + error.networkResponse.statusCode);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-type", "application/json");
                return params;
            }
        };
        mQueue.add(request);
    }
    public void deleteUserAddress(String userAddressId) {
        String url = "http://10.0.3.2:3000/userAddresses/"+userAddressId;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "delete onResponse : " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d(TAG + ": ", "delete Error Response code: " + error.networkResponse.statusCode);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-type", "application/json");
                return params;
            }
        };

        mQueue.add(request);
    }

    public boolean onBackPressed() {
        NavigationManager navigationManager=MainActivity.navigationManager;
        navigationManager.showFragment(MyAddresses.newInstance(""),false);

        return true;
    }
}
