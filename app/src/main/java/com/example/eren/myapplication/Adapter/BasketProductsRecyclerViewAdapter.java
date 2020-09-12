package com.example.eren.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eren.myapplication.Models.ShoppingCart;
import com.example.eren.myapplication.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class BasketProductsRecyclerViewAdapter extends RecyclerView.Adapter<BasketProductsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Map<String,?> allEntries;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private Map.Entry<String, ?> entry;
    private ArrayList<ShoppingCart> shoppingCarts;

    private final String URL="http://10.0.3.2:3000/";

    public BasketProductsRecyclerViewAdapter(Context context) {
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

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_basket_listitem,viewGroup,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        String as=shoppingCarts.get(i).getImageURL();
        as=as.replace("\\","/");
        Glide.with(context)
                .asBitmap()
                .load(URL+as)
                .into(viewHolder.imageView);
        viewHolder.name.setText(shoppingCarts.get(i).getName());
        viewHolder.descp.setText(shoppingCarts.get(i).getProductDescription());
        viewHolder.quantity.setText(shoppingCarts.get(i).getQuantity()+"");

        viewHolder.buton_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Emin misiniz?").setCancelable(false)
                        .setPositiveButton("Çıkart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeAt(i);
                            }
                        })
                        .setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.setTitle("Ürünü sepetten çıkarıyorsunuz.");
                alertDialog.show();
            }
        });

        viewHolder.buton_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json2 = sharedPreferences.getString(shoppingCarts.get(i).getProductId(), "");
                if((!json2.equals(""))){
                    Gson gson1=new Gson();
                    ShoppingCart obj = gson1.fromJson(json2, ShoppingCart.class);
                    obj.setQuantity((obj.getQuantity()+1));
                    String json = gson1.toJson(obj);
                    prefEditor.putString(shoppingCarts.get(i).getProductId(),json);
                    prefEditor.commit();
                    viewHolder.quantity.setText((obj.getQuantity())+"");
                }
            }
        });

        viewHolder.buton_mines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json2 = sharedPreferences.getString(shoppingCarts.get(i).getProductId(), "");
                if((!json2.equals(""))){
                    Gson gson1=new Gson();
                    ShoppingCart obj = gson1.fromJson(json2, ShoppingCart.class);

                    if( obj.getQuantity() == 1){
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setMessage("Emin misiniz?").setCancelable(false)
                                .setPositiveButton("Çıkart", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeAt(i);
                                    }
                                })
                                .setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.setTitle("Ürünü sepetten çıkarıyorsunuz.");
                        alertDialog.show();
                    }else if (obj.getQuantity()>1){
                        obj.setQuantity((obj.getQuantity()-1));
                        String json = gson1.toJson(obj);
                        prefEditor.putString(shoppingCarts.get(i).getProductId(),json);
                        prefEditor.commit();
                        viewHolder.quantity.setText(obj.getQuantity()+"");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name;
        TextView quantity;
        Button buton_remove;
        Button buton_plus;
        Button buton_mines;
        TextView descp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_basket_list_item_image);
            name=itemView.findViewById(R.id.txt_basket_list_item_name);
            quantity=itemView.findViewById(R.id.txt_basket_quantity);
            buton_remove=itemView.findViewById(R.id.btn_basket_list_item_remove);
            buton_mines=itemView.findViewById(R.id.btn_basket_mines);
            buton_plus=itemView.findViewById(R.id.btn_basket__plus);
            descp=itemView.findViewById(R.id.txt_basket_list_item_descp);
        }
    }

    public void removeAt(int position) {
        prefEditor.remove(shoppingCarts.get(position).getProductId());
        prefEditor.commit();
        shoppingCarts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, shoppingCarts.size());
    }

}