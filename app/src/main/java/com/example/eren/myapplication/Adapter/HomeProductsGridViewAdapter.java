package com.example.eren.myapplication.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eren.myapplication.Models.Products;
import com.example.eren.myapplication.Models.ShoppingCart;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeProductsGridViewAdapter extends BaseAdapter {

    private ArrayList<Products> mProducts=new ArrayList<>();
    private Context context;
    LayoutInflater layoutInflater;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    private final String URL="http://10.0.3.2:3000/";


    public HomeProductsGridViewAdapter(Context context, ArrayList<Products> products){
        this.mProducts=products;
        this.context = context;
        this.layoutInflater=LayoutInflater.from(context);
        sharedPreferences= context.getSharedPreferences("basketproducts", Context.MODE_PRIVATE);

        prefEditor = sharedPreferences.edit();

    }
    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


            convertView = layoutInflater.inflate( R.layout.layout_home_products_listitem, parent, false );

            TextView textView=convertView.findViewById(R.id.txt_home_list_name);
            ImageView imageView=convertView.findViewById(R.id.img_home_list_image);
            final TextView textView_quantity=convertView.findViewById(R.id.txt_home_quantity);
            TextView textView_des=convertView.findViewById(R.id.txt_home_list_des);
            final Button button_add=convertView.findViewById(R.id.btn_home_add);
            final Button button_mines=convertView.findViewById(R.id.btn_home_mines);
            final Button button_plus=convertView.findViewById(R.id.btn_home_plus);
            final LinearLayout linearLayout=convertView.findViewById(R.id.linearlayout_home_plusmines);



            button_mines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String json2 = sharedPreferences.getString(mProducts.get(position).getId(), "");
                    if((!json2.equals("")))
                    {
                        Gson gson1=new Gson();
                        ShoppingCart obj = gson1.fromJson(json2, ShoppingCart.class);

                        if( obj.getQuantity() == 1){
                            prefEditor.remove(mProducts.get(position).getId());
                            prefEditor.commit();

                            textView_quantity.setText(Integer.toString(0));

                            button_add.clearAnimation();
                            linearLayout.setVisibility(View.GONE);
                            button_mines.setClickable(false);
                            Animation animation = new TranslateAnimation(0, 0,0, 0);
                            animation.setDuration(500);
                            animation.setFillAfter(true);
                            animation.setFillEnabled(true);
                            button_add.startAnimation(animation);
                            button_add.clearAnimation();
                            button_add.setClickable(true);
                            button_add.setVisibility(View.VISIBLE);
                        }else if (obj.getQuantity()>1){
                            obj.setQuantity((obj.getQuantity()-1));
                            String json = gson1.toJson(obj);
                            prefEditor.putString(mProducts.get(position).getId(),json);
                            prefEditor.commit();
                            textView_quantity.setText(obj.getQuantity()+"");
                        }
                    }


                }
            });

            button_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String json2 = sharedPreferences.getString(mProducts.get(position).getId(), "");
                    if((!json2.equals(""))){
                        Gson gson1=new Gson();
                        ShoppingCart obj = gson1.fromJson(json2, ShoppingCart.class);
                        obj.setQuantity((obj.getQuantity()+1));
                        String json = gson1.toJson(obj);
                        prefEditor.putString(mProducts.get(position).getId(),json);
                        prefEditor.commit();
                        textView_quantity.setText((obj.getQuantity())+"");
                    }
                }
            });


            button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    button_add.clearAnimation();
                    Animation animation = new TranslateAnimation(0, 500,0, 0);
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    animation.setFillEnabled(true);
                    button_add.startAnimation(animation);
                    button_add.setClickable(false);
                    button_add.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    button_mines.setClickable(true);


                    ShoppingCart shoppingCart=new ShoppingCart(mProducts.get(position).getId(),
                            mProducts.get(position).getName(),
                            mProducts.get(position).getProductImage(),
                            1);
                    shoppingCart.setProductDescription(mProducts.get(position).getDescription());
                    Gson gson=new Gson();
                    String json = gson.toJson(shoppingCart);
                    prefEditor.putString(mProducts.get(position).getId(),json);
                    prefEditor.commit();


                    textView_quantity.setText(1+"");

                }
            });




            String json3 = sharedPreferences.getString(mProducts.get(position).getId(), "");
            if((!json3.equals(""))){ //EĞER SEPETTE VAR İSE


                button_add.setVisibility(View.GONE);
                button_add.clearAnimation();
                Animation animation = new TranslateAnimation(0, 500,0, 0);
                animation.setDuration(0);
                animation.setFillAfter(true);
                animation.setFillEnabled(true);
                button_add.startAnimation(animation);
                button_add.setClickable(false);
                linearLayout.setVisibility(View.VISIBLE);
                button_mines.setClickable(true);

                Gson gson1=new Gson();
                ShoppingCart obj = gson1.fromJson(json3, ShoppingCart.class);
                textView_quantity.setText(obj.getQuantity()+"");
            }else {
                linearLayout.setVisibility(View.GONE);
                linearLayout.setClickable(false);


                button_add.clearAnimation();
                linearLayout.setVisibility(View.GONE);
                button_mines.setClickable(false);
                Animation animation = new TranslateAnimation(0, 0,0, 0);
                animation.setDuration(500);
                animation.setFillAfter(true);
                animation.setFillEnabled(true);
                button_add.startAnimation(animation);
                button_add.clearAnimation();
                button_add.setClickable(true);
                button_add.setVisibility(View.VISIBLE);
            }
            textView.setText(mProducts.get(position).getName());
            textView_des.setText(mProducts.get(position).getDescription());
            String as=mProducts.get(position).getProductImage();
            as=as.replace("\\","/");
            Glide.with(convertView)
                    .asBitmap()
                    .load(URL+as)
                    .into(imageView);


        return convertView;
    }

    public void clearAdapterData() {
        mProducts.clear();
        notifyDataSetChanged();
    }
}
