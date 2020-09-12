package com.example.eren.myapplication.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.PaymentListAdapter;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.Models.PaymentModel;
import com.example.eren.myapplication.Models.ShopUnit;
import com.example.eren.myapplication.Models.ShopUnitStock;
import com.example.eren.myapplication.Models.ShoppingCart;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPayment extends Fragment implements IOnBackPressed{

    private NavigationManager navigationManager;
    private ListView lvPayments;
    private PaymentListAdapter paymentListAdapter;
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<Drawable> icons=new ArrayList<>();
    ListView listView;
    private static final String NEIGHBOORHOOD="mahalleId";
    public RequestQueue mQueue;
    private Button btn_buy;

    private ArrayList<ShopUnitStock> mShopUnitStocks=new ArrayList<>();
    private ShopUnit mShopUnit;
    private String payment_type="0";
    private ArrayList<ShoppingCart> shoppingCarts;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private Map<String,?> allEntries;
    private ArrayList<PaymentModel> paymentModels=new ArrayList<>();
    SharedPreferences sharedPreferences2 ;
    SharedPreferences sharedPreferences35;

    public ShopUnit getmShopUnit() {
        return mShopUnit;
    }

    public void setmShopUnit(ShopUnit mShopUnit) {
        this.mShopUnit = mShopUnit;
    }

    public FragmentPayment() {
        // Required empty public constructor
    }
    public static FragmentPayment newInstance(String neighboorhood) {
        FragmentPayment fragment = new FragmentPayment();
      //  Bundle args = new Bundle();
     //   args.putString(NEIGHBOORHOOD, neighboorhood);
     //   fragment.setArguments(args);

        return fragment;
    }

    public ArrayList<ShopUnitStock> getmShopUnitStocks() {
        return mShopUnitStocks;
    }

    public void setmShopUnitStocks(ArrayList<ShopUnitStock> mShopUnitStocks) {
        this.mShopUnitStocks = mShopUnitStocks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mQueue = Volley.newRequestQueue(getContext());

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sharedPreferences35= getContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);
        navigationManager= MainActivity.navigationManager;
        sharedPreferences2= getActivity().getSharedPreferences("marketadres", Context.MODE_PRIVATE);
        shoppingCarts=new ArrayList<>();
        sharedPreferences= getActivity().getSharedPreferences("basketproducts", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
        allEntries = sharedPreferences.getAll();


        Gson gson=new Gson();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            ShoppingCart obj = gson.fromJson(entry.getValue().toString(), ShoppingCart.class);
            shoppingCarts.add(obj);
        }


        View view=inflater.inflate(R.layout.fragment_fragment_payment, container, false);
        listView=view.findViewById(R.id.payment_liste);
        btn_buy=view.findViewById(R.id.btn_payment_buy);

        titles.add("Kapıda Kredi Kartı");
        titles.add("Kapıda Nakit");
        icons.add(getResources().getDrawable(R.drawable.ic_credit_card_black_24dp));
        icons.add(getResources().getDrawable(R.drawable.ic_attach_money_black_24dp));
        paymentListAdapter=new PaymentListAdapter(getContext(),titles,icons);
        listView.setAdapter(paymentListAdapter);





        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



                Long toplam=0L;
                for(int i=0;i<mShopUnitStocks.size();i++){
                    for(int j=0;j<shoppingCarts.size();j++){
                        if(shoppingCarts.get(j).getProductId().equals(mShopUnitStocks.get(i).getProduct())){
                            Long q=new Long(shoppingCarts.get(j).getQuantity());
                            toplam+=mShopUnitStocks.get(i).getPrice()*q;
                            Long t2=mShopUnitStocks.get(i).getPrice()*q;
                            PaymentModel p=new PaymentModel(mShopUnitStocks.get(i).getProduct()
                                    ,Integer.toString(shoppingCarts.get(j).getQuantity())
                                    ,Long.toString(t2)
                                    ,Integer.toString(mShopUnitStocks.get(i).getStock())
                                    ,mShopUnitStocks.get(i).get_id());
                            paymentModels.add(p);
                        }
                    }
                }
                String toplamS=String.valueOf(toplam);

                String userAdres= sharedPreferences2.getString("selectedAddress","");


                String userIDlogin=sharedPreferences35.getString("userid","");
                createOrder(mShopUnit.get_id(),userIDlogin,toplamS,payment_type,userAdres);



                sharedPreferences.edit().clear().apply();
                sharedPreferences2.edit().clear().apply();

                MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                paymentListAdapter.setmSelectedItem(position);
                paymentListAdapter.notifyDataSetChanged();
                if(position==0){
                    payment_type="0";
                }else if(position==1){
                    payment_type="1";
                }
            }
        });
    }

    private void updateStock(String shopunitid,String productId,String quantity){
        OkHttpClient clientUserAddress = new OkHttpClient();

        MediaType mediaType2 = MediaType.parse("application/json");
        RequestBody body2 = RequestBody.create(mediaType2, "[{\"propName\": \"stock\",\"value\":\""+quantity+"\"}]");
        okhttp3.Request request2 = new okhttp3.Request.Builder()
                .url("http://10.0.3.2:3000/shopUnitStocks/"+shopunitid+"?product="+productId)
                .patch(body2)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            okhttp3.Response response2 = clientUserAddress.newCall(request2).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createStockMovement(final String shopunitid, final String productId, final String quantity){
        String url = "http://10.0.3.2:3000/shopUnitStockmovements";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("shopUnit", shopunitid);
                MyData.put("product", productId);
                MyData.put("quantity",quantity);
                MyData.put("movement_type", "0");

                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }


    private void createOrder(final String shopUnit, final String userId, final String totalAmount, final String paymentType, final String userAddress)
    {
        String url = "http://10.0.3.2:3000/orders";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    JSONObject orderResponse = obj.getJSONObject("createdOrder");
                    String orderId = orderResponse.getString("_id");
                    for(int k=0;k<paymentModels.size();k++){
                        createOrderDetail(orderId,paymentModels.get(k).getProduct(),paymentModels.get(k).getQuantity(),
                                paymentModels.get(k).getAmount());
                        updateStock(paymentModels.get(k).getStockId(),paymentModels.get(k).getProduct(),paymentModels.get(k).getStock());
                        createStockMovement(shopUnit, paymentModels.get(k).getProduct(), paymentModels.get(k).getQuantity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sipariş Oluşturulamadı", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("shopUnit", shopUnit);
                MyData.put("guid", "100");
                MyData.put("user",userId);
                MyData.put("total_amount", totalAmount);
                MyData.put("payment_type", paymentType);
                MyData.put("address", userAddress);
                MyData.put("order_status", "0");
                MyData.put("order_active", "0");

                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }
    private void createOrderDetail(final String orderId, final String product,final String quantity,final String amount)
    {
        String url = "http://10.0.3.2:3000/orderDetails";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sipariş Oluşturulamadı", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("order", orderId);
                MyData.put("product", product);
                MyData.put("quantity",quantity);
                MyData.put("amount", amount);

                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }

    @Override
    public boolean onBackPressed() {
        navigationManager.showFragment(FragmentBasket.newInstance(""),false);

        return true;
    }
}
