package com.example.eren.myapplication.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eren.myapplication.Models.Products;
import com.example.eren.myapplication.Models.ShopUnit;
import com.example.eren.myapplication.Models.ShopUnitStock;
import com.example.eren.myapplication.Models.ShoppingCart;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class MarketsProductRecyclerViewAdapter extends RecyclerView.Adapter<MarketsProductRecyclerViewAdapter.ViewHolder>{


    private ArrayList<String> mNames=new ArrayList<>();
    private ArrayList<String> mImageUrls=new ArrayList<>();

    private ArrayList<Products> mProducts=new ArrayList<>();
    Map<String,ShopUnit> mShopMap=new TreeMap<>();
    private Context context;
    private final String URL="http://10.0.3.2:3000/";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private Map<String,?> allEntries;
    private ArrayList<ShoppingCart> shoppingCarts;
    private ArrayList<ShopUnitStock> mShopUnitStocks=new ArrayList<>();
    public MarketsProductRecyclerViewAdapter(Context context, ArrayList<ShopUnitStock> mShopUnitStocks) {
        this.mShopUnitStocks = mShopUnitStocks;
        this.context = context;

        shoppingCarts=new ArrayList<>();
        sharedPreferences= context.getSharedPreferences("basketproducts", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
        allEntries = sharedPreferences.getAll();


        Gson gson=new Gson();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            ShoppingCart obj = gson.fromJson(entry.getValue().toString(), ShoppingCart.class);
            shoppingCarts.add(obj);
        }



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_market__products_listitem,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {


        ShoppingCart shoppingCart=null;
        ShopUnitStock shopUnitStock=null;
        for(int s=0;s<mShopUnitStocks.size();s++){
            if(mShopUnitStocks.get(s).getProduct().equals(shoppingCarts.get(i).getProductId())){
                shoppingCart=shoppingCarts.get(i);
                shopUnitStock=mShopUnitStocks.get(s);
            }
        }
        if(shoppingCart!=null){
            String as=shoppingCart.getImageURL();
            as=as.replace("\\","/");
            Glide.with(context)
                    .asBitmap()
                    .load(URL+as)
                    .into(viewHolder.circleImageView);

            viewHolder.txt_name.setText(shoppingCart.getName());
            viewHolder.txt_price.setText("Br."+shopUnitStock.getPrice()+"₺");
            viewHolder.txt_descp.setText(shoppingCart.getProductDescription());
            viewHolder.txt_descp.setTextColor(ContextCompat.getColor(context, R.color.list_item_color));

            Long b=new Long(shoppingCart.getQuantity());
            viewHolder.txt_quantity.setText(b+"");
            Long a=shopUnitStock.getPrice();
            Long c=a*b;
            viewHolder.txt_totelprice.setText(c+"₺");


            viewHolder.linearLayout_butonpanel.setVisibility(View.VISIBLE);
            viewHolder.txt_totelprice.setVisibility(View.VISIBLE);

        }else{
            viewHolder.txt_name.setText(shoppingCarts.get(i).getName());
            viewHolder.txt_descp.setText("Ürün markette bulunmuyor.");
            viewHolder.txt_descp.setTextColor(Color.RED);
            String as=shoppingCarts.get(i).getImageURL();
            as=as.replace("\\","/");
            Glide.with(context)
                    .asBitmap()
                    .load(URL+as)
                    .into(viewHolder.circleImageView);
            viewHolder.linearLayout_butonpanel.setVisibility(View.GONE);
            viewHolder.txt_totelprice.setVisibility(View.GONE);
        }





    }
    @Override
    public int getItemCount() {

        return shoppingCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView circleImageView;
        TextView txt_name;
        TextView txt_price;
        LinearLayout linearLayout_butonpanel;
        TextView txt_totelprice;
        TextView txt_quantity;
        TextView txt_descp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.img_list_item_image);
            txt_name=itemView.findViewById(R.id.txt_list_item_name);
            txt_price=itemView.findViewById(R.id.txt_list_item_price);
            txt_quantity=itemView.findViewById(R.id.txt_market_quantity);
            txt_totelprice=itemView.findViewById(R.id.txt_market_listitem_totalprice);
            linearLayout_butonpanel=itemView.findViewById(R.id.linearlayout_market_butonpanel);
            txt_descp=itemView.findViewById(R.id.txt_market_list_item_descp);

        }
    }
    public void clearAdapterData() {
        mShopUnitStocks.clear();
        notifyDataSetChanged();
    }

}
