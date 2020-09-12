package com.example.eren.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.Models.District;
import com.example.eren.myapplication.Models.Neighborhood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private RequestQueue mQueue;

    private Spinner spn_cities,spn_districts,spn_neighborhoods;
    private EditText et_register_email,et_register_password,et_register_phone,et_register_address;

    private ArrayList<City> cities;
    private ArrayList<District> districts,districtsArray;
    private ArrayList<Neighborhood> neighborhoods,neighborhoodsArray;

    private String selectCity="";
    private String selectDistrict="";
    private String selectNeighborhood="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mQueue = Volley.newRequestQueue(this);

        TextView btn_register_login = findViewById(R.id.btn_register_login);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        spn_cities = findViewById(R.id.spn_register_city);
        spn_districts = findViewById(R.id.spn_register_district);
        spn_neighborhoods = findViewById(R.id.spn_register_neighborhood);
        et_register_email = findViewById(R.id.et_register_email);
        et_register_password = findViewById(R.id.et_register_password);
        et_register_phone = findViewById(R.id.et_register_phone);
        et_register_address = findViewById(R.id.et_register_address);


        spn_neighborhoods.getBackground().setColorFilter(getResources().getColor(R.color.spinner_text), PorterDuff.Mode.SRC_ATOP);
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


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( et_register_email.getText().toString().trim().equals("") || et_register_password.getText().toString().trim().equals("") || et_register_phone.getText().toString().trim().equals("") || et_register_address.getText().toString().trim().equals("") || spn_cities.getCount()==0 || spn_districts.getCount()==0 || spn_neighborhoods.getCount()==0)
                {
                    Toast.makeText(getApplicationContext(), "Lütfen Gerekli Alanları Doldurunuz", Toast.LENGTH_SHORT).show();

                } else {
                    Log.i("@@@@@@@@@@@", et_register_email.getText()+" "+et_register_password.getText()+" "+et_register_address.getText()+" "+et_register_phone.getText()+" "+selectCity+" "+selectDistrict+" "+selectNeighborhood);
                    //postAddNewAddress(selectCity,selectDistrict,selectNeighborhood,et_description.getText().toString());
                    String email = et_register_email.getText().toString();
                    String password = et_register_password.getText().toString();
                    String address = et_register_address.getText().toString();
                    String phone = et_register_phone.getText().toString();

                    postSignUp(email,password,phone);

                }
            }
        });

        btn_register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                killActivity();
            }
        });
    }

    private void getCities(){
        String url = "http://10.0.3.2:3000/cities";
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
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
                }, new com.android.volley.Response.ErrorListener() {
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
            adapter = new ArrayAdapter<City>(this, android.R.layout.simple_spinner_item, cities){

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    ((TextView) v).setTextSize(16);
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.spinner_text)
                    );

                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);


                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.white)
                    );

                    ((TextView) v).setGravity(Gravity.CENTER);

                    return v;
                }
            };



            spn_cities.getBackground().setColorFilter(getResources().getColor(R.color.spinner_text), PorterDuff.Mode.SRC_ATOP);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_cities.setAdapter(adapter);
        }
    }

    public void getDistricts(final String cityCode){
        String url = "http://10.0.3.2:3000/districts";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
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
                }, new com.android.volley.Response.ErrorListener() {
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
            adapter = new ArrayAdapter<District>(this,android.R.layout.simple_list_item_1,districtsArray){

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    ((TextView) v).setTextSize(16);
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.spinner_text)
                    );

                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);


                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.white)
                    );

                    ((TextView) v).setGravity(Gravity.CENTER);

                    return v;
                }
            };



            spn_districts.getBackground().setColorFilter(getResources().getColor(R.color.spinner_text), PorterDuff.Mode.SRC_ATOP);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_districts.setAdapter(adapter);
        }
    }

    public void getNeighborhoods(final String districtId){
        String url = "http://10.0.3.2:3000/neighborhoods";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
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
                }, new com.android.volley.Response.ErrorListener() {
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
            adapter = new ArrayAdapter<Neighborhood>(this,android.R.layout.simple_list_item_1,neighborhoodsArray){

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    ((TextView) v).setTextSize(16);
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.spinner_text)
                    );

                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);


                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.white)
                    );

                    ((TextView) v).setGravity(Gravity.CENTER);

                    return v;
                }
            };



            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_neighborhoods.setAdapter(adapter);
        }
    }

    private void postSignUp(final String email, final String password,final String phone)
    {
        String url = "http://10.0.3.2:3000/user/signup";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    String userId = obj.getString("id"); //Token localstorage a yazılacak
                    postSignUpUserAddress(selectCity,selectDistrict,selectNeighborhood,et_register_address.getText().toString(),userId);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("email", email); //Add the data you'd like to send to the server.
                MyData.put("password", password);
                MyData.put("phone", phone);
                MyData.put("userType", "5c1d170270920532900b6a5e");
                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }
    private void postSignUpUserAddress(final String city, final String district,final String neighborhood,final String description, final String userId)
    {
        String url = "http://10.0.3.2:3000/addresses";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    JSONObject userIdObject = obj.getJSONObject("createdAddress");
                    String userAddressId = userIdObject.getString("_id");
                    postUserAddress(userId,userAddressId);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
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
    private void postUserAddress(final String userId,final String userAddress)
    {
        String url = "http://10.0.3.2:3000/userAddresses";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    String userId = obj.getString("id"); //Token localstorage a yazılacak
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    killActivity();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("user", userId); //Add the data you'd like to send to the server.
                MyData.put("address", userAddress);
                MyData.put("name", "Ev");
                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }

    private void killActivity(){

        finish();
    }
}
