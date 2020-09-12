package com.example.eren.myapplication.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eren.myapplication.Models.OrderDetail;
import com.example.eren.myapplication.R;

import java.util.ArrayList;
import java.util.Map;


public class OrderDetailRecyclerViewAdapter extends RecyclerView.Adapter<OrderDetailRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<OrderDetail> mOrderDetail = new ArrayList<>();
    private Context mContext;
    private String url = "http://10.0.3.2:3000/";

    private Map<String,OrderDetail> mapOrderDetail;

    public OrderDetailRecyclerViewAdapter(ArrayList<OrderDetail> mOrderDetail, Context mContext,Map<String,OrderDetail> mapOrderDetail) {
        this.mOrderDetail = mOrderDetail;
        this.mContext = mContext;
        this.mapOrderDetail=mapOrderDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_order_detail_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        String as=mOrderDetail.get(position).getProductImage();
        as=as.replace("\\","/");
        Glide.with(mContext)
                .asBitmap()
                .load(url+as)
                .into(holder.image);
        Long l =new Long(mOrderDetail.get(position).getAmount());
        Long sonuc=l/new Long(mOrderDetail.get(position).getQuantity());
        holder.tv_productName.setText(mOrderDetail.get(position).getProducts());
        holder.tv_productQuantity.setText("Adet: "+mOrderDetail.get(position).getQuantity().toString());
        holder.tv_productAmount.setText(mOrderDetail.get(position).getQuantity().toString()+" x "+
                sonuc+" ₺");
        int a = Integer.parseInt(mOrderDetail.get(position).getAmount());
        holder.tv_productTotal.setText("Ara Toplam: "+Integer.toString(a)+" ₺");

    }

    @Override
    public int getItemCount() {
        return mOrderDetail.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView tv_productName, tv_productAmount, tv_productQuantity,tv_productTotal;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.img_product);
            tv_productName = itemView.findViewById(R.id.tv_productName);
            tv_productAmount = itemView.findViewById(R.id.tv_productAmount);
            tv_productQuantity = itemView.findViewById(R.id.tv_productQuantity);
            tv_productTotal = itemView.findViewById(R.id.tv_productTotal);

        }
    }
}
