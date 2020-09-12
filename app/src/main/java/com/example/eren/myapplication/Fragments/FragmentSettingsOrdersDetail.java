package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.example.eren.myapplication.Adapter.OrderDetailRecyclerViewAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.OrderDetail;
import com.example.eren.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettingsOrdersDetail extends Fragment implements IOnBackPressed {

    private static final String TAG = FragmentSettingsOrdersDetail.class.getSimpleName();
    private RequestQueue mQueue;
    RecyclerView recyclerView;

    public Map<String,OrderDetail> mapOrderDetail;

    private OrderDetailRecyclerViewAdapter adapter;
    private ArrayList<OrderDetail> orderDetails,mOrders;

    public FragmentSettingsOrdersDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_settings_orders_detail, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.ryc_orderDetail);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        String id=sharedPref.getString("orderId","");

        getUserOrderDetails(id);
    }

    public void getUserOrderDetails(String orderId){
        String url = "http://10.0.3.2:3000/orderdetails/?guid="+orderId;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        orderDetails = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("orderDetails");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonOrderDetail = jsonArray.getJSONObject(i);
                                String id = jsonOrderDetail.getString("_id");
                                String created_at = jsonOrderDetail.getString("created_at");
                                String product = jsonOrderDetail.getString("product");
                                String quantity = jsonOrderDetail.getString("quantity");
                                String amount = jsonOrderDetail.getString("amount");
                                String productImage="";
                                OrderDetail p=new OrderDetail(id,product,created_at,quantity,amount,productImage);
                                orderDetails.add(p);
                            }
                            refreshData_getUserOrderDetails();

                            adapter = new OrderDetailRecyclerViewAdapter(mOrders,getContext(),mapOrderDetail);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

    public void refreshData_getUserOrderDetails() {
        mapOrderDetail=new TreeMap<>();
        mOrders = new ArrayList<>();

        if (orderDetails.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {
            for (int i = 0; i < orderDetails.size(); i++)
                mapOrderDetail.put(orderDetails.get(i).getProducts(),orderDetails.get(i));
            for (int i = 0; i < orderDetails.size(); i++) {
                getProductProperty(orderDetails.get(i).getProducts());
            }
        }
    }

    public void getProductProperty(final String productId){
        String url = "http://10.0.3.2:3000/products/"+productId;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonShopUnitName = response.getJSONObject("product");
                            String productId = jsonShopUnitName.getString("_id");
                            String productName = jsonShopUnitName.getString("name");
                            String productImage = jsonShopUnitName.getString("productImage");

                            OrderDetail olr=mapOrderDetail.get(productId);
                            mOrders.add(new OrderDetail(olr.getId(),productName,olr.getCreated_at(),olr.getQuantity(),olr.getAmount(),productImage));
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

    public NavigationManager navigationManager;
    @Override
    public boolean onBackPressed() {
        navigationManager= MainActivity.navigationManager;

        navigationManager.showFragment(FragmentSettingsOrders.newInstance(""),false);

        return true;
    }
}
