package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.MarketsProductRecyclerViewAdapter;
import com.example.eren.myapplication.Models.ShopUnit;
import com.example.eren.myapplication.Models.ShopUnitStock;
import com.example.eren.myapplication.Models.ShoppingCart;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.example.eren.myapplication.MainActivity.navigationManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMarketsProducts extends Fragment {

    ShopUnit shops=new ShopUnit();
    Map<String,ShopUnit> shopMap=new TreeMap<>();
    RecyclerView recyclerView;
    MarketsProductRecyclerViewAdapter adapter;
    private ArrayList<ShopUnitStock> mShopUnitStocks=new ArrayList<>();
    public RequestQueue mQueue;
    int pos;
    private ArrayList<ShoppingCart> shoppingCarts;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private Map<String,?> allEntries;
    Context context;
    TextView txt_genel_toplam;
    TextView txt_min_order;
    Long toplam=new Long(0);
    Button btn_buy;

    public FragmentMarketsProducts() {
        // Required empty public constructor

    }
    public FragmentMarketsProducts setShops(ShopUnit shops){
        this.shops=shops;

        return this;
    }
    public FragmentMarketsProducts shopMap(Map<String,ShopUnit> shopMap){
        this.shopMap=shopMap;

        return this;
    }
    public FragmentMarketsProducts setPos(int i){
        pos=i;

        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shoppingCarts=new ArrayList<>();
        sharedPreferences= getActivity().getSharedPreferences("basketproducts", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
        allEntries = sharedPreferences.getAll();


        Gson gson=new Gson();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            ShoppingCart obj = gson.fromJson(entry.getValue().toString(), ShoppingCart.class);
            shoppingCarts.add(obj);
        }



        View view=inflater.inflate(R.layout.fragment_fragment_markets_products, container, false);
        txt_genel_toplam=view.findViewById(R.id.txt_market_list_item_subprice);
        txt_genel_toplam.setText(toplam+"₺");
        txt_min_order=view.findViewById(R.id.txt_min_order);
        txt_min_order.setText(getResources().getString(R.string.min_order)+shops.getMin_order()+"₺");

        btn_buy=view.findViewById(R.id.btn_market_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toplam<shops.getMin_order()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("Minimum sipariş tutarı "+shops.getMin_order()+"₺.").setCancelable(false)
                            .setPositiveButton("Anladım", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("Lütfen marketin minimum sipariş tutarını geçiniz.");
                    alertDialog.show();
                }else{
                    FragmentPayment x=FragmentPayment.newInstance("");
                    x.setmShopUnitStocks(mShopUnitStocks);
                    x.setmShopUnit(shops);
                    navigationManager.showFragment(x,false);
                }



            }
        });


        recyclerView=view.findViewById(R.id.recyclerView_markets_products);
        adapter=new MarketsProductRecyclerViewAdapter(inflater.getContext(),mShopUnitStocks);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(inflater.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(adapter);

        adapter.clearAdapterData();
        for(int j=0;j<shoppingCarts.size();j++){
            setProducts(shoppingCarts.get(j).getProductId(),shops,shoppingCarts.get(j).getQuantity());
            adapter.notifyDataSetChanged();
        }
        // Inflate the layout for this fragment
        return view;
    }

    public void setProducts(String productID, ShopUnit mShopUnit, final Integer quantity){

        String url = "http://10.0.3.2:3000/shopUnitstocks?shopUnit="+mShopUnit.get_id()+"&product="+productID;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonAddressesDetail = response.getJSONArray("shopUnitStocks");
                            for (int i = 0; i < jsonAddressesDetail.length(); i++) {
                                JSONObject product = jsonAddressesDetail.getJSONObject(i);
                                String id = product.getString("_id");
                                String shopUnit = product.getString("shopUnit");
                                String product2 = product.getString("product");
                                Integer stock = product.getInt("stock");
                                Long price = product.getLong("price");
                                Boolean discount = product.getBoolean("discount");
                                Integer discount_rate = product.getInt("discount_rate");
                                Boolean campaign = product.getBoolean("campaign");

                                ShopUnitStock p = new ShopUnitStock(id, shopUnit, product2, stock, price, discount, discount_rate, campaign);
                                mShopUnitStocks.add(p);
                                toplam+=p.getPrice()*(new Long(quantity));
                                txt_genel_toplam.setText(toplam+"₺");
                            }
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

}
