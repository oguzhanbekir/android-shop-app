package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.example.eren.myapplication.Adapter.OrdersListAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.Order;
import com.example.eren.myapplication.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettingsOrders extends Fragment implements IOnBackPressed {
    private static final String TAG = FragmentSettingsOrders.class.getSimpleName();
    private RequestQueue mQueue;

    private ArrayList<Order> orders, mOrders;
    public Map<String,Order> mapOrder;
    public Map<String,Order> mapOrders=new TreeMap<>();
    public ListView lvSettingsOrders;
    private OrdersListAdapter adapter;
    SharedPreferences sharedPreferences35;

    public FragmentSettingsOrders() {
        // Required empty public constructor
    }
    public static FragmentSettingsOrders newInstance(String param1) {
        FragmentSettingsOrders fragment = new FragmentSettingsOrders();
        //   Bundle args = new Bundle();
        // args.putString(KEY_TITLE, param1);
        //fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_settings_orders, container, false);

        sharedPreferences35= getContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);
        lvSettingsOrders = view.findViewById(R.id.lv_settingsOrders);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        String userIDlogin=sharedPreferences35.getString("userid","");
        getOrders(userIDlogin);
        lvSettingsOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentSettingsOrdersDetail fragment = new FragmentSettingsOrdersDetail();
                fm.beginTransaction().replace(R.id.container,fragment).commit();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("orderId",view.getTag(R.id.orderId).toString());
                editor.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void getOrders(String userId){
        String url = "http://10.0.3.2:3000/orders?user="+userId;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        orders = new ArrayList<>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("orders");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String payment_type2= "";
                                String order_status2= "";
                                JSONObject jsonOrders = jsonArray.getJSONObject(i);
                                String id = jsonOrders.getString("_id");
                                String created_at = jsonOrders.getString("created_at");
                                String shopUnit = jsonOrders.getString("shopUnit");
                                int total_amount = jsonOrders.getInt("total_amount");
                                int payment_type = jsonOrders.getInt("payment_type");
                                int order_status = jsonOrders.getInt("order_status");

                                if(payment_type==0){
                                    payment_type2 = "Kredi Kartı";
                                } else if(payment_type==1){
                                    payment_type2 = "Nakit";
                                }

                                if(order_status==0){
                                    order_status2="Onay Bekliyor";
                                } else if(order_status==1){
                                    order_status2="Siparişiniz Hazırlandı";
                                } else if(order_status==2){
                                    order_status2="Alıcıya Ulaştırılmak Üzere Yola Çıktı";
                                } else if(order_status==3){
                                    order_status2="Teslim Edildi";
                                } else if(order_status==4){
                                    order_status2="İptal Edildi";
                                }

                                String created_at2 = created_at.split("T")[0];

                                Order p=new Order(id,created_at2,shopUnit,total_amount,payment_type2,order_status2);
                                orders.add(p);
                            }
                            refreshData_getOrders();

                            adapter = new OrdersListAdapter(getContext(),mOrders,mapOrder);
                            lvSettingsOrders.setAdapter(adapter);


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


    public void refreshData_getOrders() {
        mOrders = new ArrayList<>();
        mapOrder=new TreeMap<>();

        if (orders.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {

            for (int i = 0; i < orders.size(); i++)
                mapOrder.put(orders.get(i).getId(),orders.get(i));

            for (int i = 0; i < orders.size(); i++) {
                getShopUnitName(orders.get(i).getShopUnit(),orders.get(i).getId());
            }
        }
    }

    public void getShopUnitName(final String shopUnit,final String orderId){
        String url = "http://10.0.3.2:3000/shopunits/"+shopUnit;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonShopUnitName = response.getJSONObject("shopUnit");
                            String shopUnitName = jsonShopUnitName.getString("name");
                            String shopUnitId = jsonShopUnitName.getString("_id");

                            Order olr=mapOrder.get(orderId);
                            mOrders.add(new Order(olr.getId(),olr.getCreated_at(),shopUnitName,olr.getTotal_amount(),olr.getPayment_type(),olr.getOrder_status()));

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

        navigationManager.showFragment(FragmentSettings.newInstance(""),false);

        return true;
    }
}
