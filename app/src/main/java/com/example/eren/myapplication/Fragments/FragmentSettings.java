package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.UserSubCategoryListAdapter;
import com.example.eren.myapplication.Adapter.UserTopInfoListAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.LoginActivity;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.User;
import com.example.eren.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment implements IOnBackPressed {
    private RequestQueue mQueue;

    private ListView lv_userInfo,lv_subCategory;

    private ArrayList<User> mUser;

    private UserTopInfoListAdapter adapter;
    private UserSubCategoryListAdapter adapterSubCategory;

    private ArrayList<String> subCategory = new ArrayList<>();
    private ArrayList<Drawable>  mVector = new ArrayList<>();

    private NavigationManager navigationManager;
    SharedPreferences sharedPreferences35;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;

    public FragmentSettings() {
        // Required empty public constructor
    }
    public static FragmentSettings newInstance(String param1) {
        FragmentSettings fragment = new FragmentSettings();
        //   Bundle args = new Bundle();
        // args.putString(KEY_TITLE, param1);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_settings, container, false);
        navigationManager= MainActivity.navigationManager;

        sharedPreferences2= getContext().getSharedPreferences("marketadres", Context.MODE_PRIVATE);
        sharedPreferences= getContext().getSharedPreferences("basketproducts", Context.MODE_PRIVATE);
        sharedPreferences35= getContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);
        lv_userInfo = view.findViewById(R.id.lv_userInfo);
        lv_subCategory=view.findViewById(R.id.lv_userSubCategory);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String userIDlogin=sharedPreferences35.getString("userid","");
        getUserInfo(userIDlogin);

        subCategory.add("Adreslerim");
        mVector.add(getResources().getDrawable(R.drawable.ic_location_on_black_24dp));
        subCategory.add("Siparişlerim");
        mVector.add(getResources().getDrawable(R.drawable.ic_shopping_basket_black_24dp));
        subCategory.add("Şifre Değiştir");
        mVector.add(getResources().getDrawable(R.drawable.ic_lock_black_24dp));
        subCategory.add("Çıkış Yap");
        mVector.add(getResources().getDrawable(R.drawable.ic_close_black_24dp));

        adapterSubCategory = new UserSubCategoryListAdapter(getContext(),subCategory,mVector);
        lv_subCategory.setAdapter(adapterSubCategory);

        lv_subCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: navigationManager.showFragment(MyAddresses.newInstance(""),false);
                    break;
                    case 1: navigationManager.showFragment(FragmentSettingsOrders.newInstance(""),false);
                    break;
                    case 2:navigationManager.showFragment(FragmentChangePassword.newInstance(""),false);
                    break;
                    case 3:
                        sharedPreferences2.edit().clear().apply();
                        sharedPreferences.edit().clear().apply();
                        sharedPreferences35.edit().clear().apply();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                }
            }
        });
    }

    public void getUserInfo(String userId){
        String url = "http://10.0.3.2:3000/user/"+userId;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUser = new ArrayList<>();
                        try {
                            String userId = response.getString("Id");
                            String userEmail = response.getString("email");
                            String userPhone = response.getString("phone");
                            User p=new User(userId,userEmail,userPhone);
                            mUser.add(p);

                            adapter = new UserTopInfoListAdapter(getContext(),mUser);
                            lv_userInfo.setAdapter(adapter);


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

        return true;
    }

}
