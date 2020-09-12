package com.example.eren.myapplication.Fragments;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.HomeProductsGridViewAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.Products;
import com.example.eren.myapplication.R;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProducts extends Fragment implements IOnBackPressed {


    private static final String CATEGORYID="Category";

    HomeProductsGridViewAdapter adapter;
    GridView gridView;
    String category="";
    private ArrayList<String> mNames=new ArrayList<>();
    private ArrayList<String> mImageUrls=new ArrayList<>();
    private ArrayList<String> mDescriptions=new ArrayList<>();
    public static final String TAG = Products.class.getSimpleName();
    public RequestQueue mQueue;
    public ArrayList<Products> products;
    public ArrayList<Products> productsSet=new ArrayList<>();
    public SwipyRefreshLayout swipeRefreshLayout;
    private int page=1;
    private int limit=20;

    public FragmentProducts() {

    }

    public static FragmentProducts newInstance(String category) {
        FragmentProducts fragment = new FragmentProducts();
        Bundle args = new Bundle();
        args.putString(CATEGORYID, category);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_products, container, false);
        gridView=view.findViewById(R.id.gridview_home_list_products);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefresh_layout_products);
        swipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        // Inflate the layout for this fragment
        adapter=new HomeProductsGridViewAdapter(getContext(),productsSet);
        gridView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                page=page+1;
                setPage(page);
                setProducts();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        category=getArguments().getString(CATEGORYID);



        setPage(1);
        setProducts();

    }

    public void setPage(int i){
        page=i;
    }

    public void setProducts(){
        String url = "http://10.0.3.2:3000/products?category="+category+"&limit="+limit+"&page="+page;
        mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("products");
                            products=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject product = jsonArray.getJSONObject(i);
                                String id = product.getString("_id");
                                String name= product.getString("name");
                                String productImage = product.getString("productImage");
                                String description = product.getString("description");
                                Products p=new Products(id,name,productImage,description);
                                products.add(p);
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

        if (products.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {
            for (int i = 0; i < products.size(); i++) {
                productsSet.add(products.get(i));
            }

        }

        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }

    public NavigationManager navigationManager;
    @Override
    public boolean onBackPressed() {
        navigationManager= MainActivity.navigationManager;

        navigationManager.showFragment(FragmentProducts.newInstance(""),false);
        MainActivity.actionBar.setTitle("Ürünler");
        return true;
    }


}
