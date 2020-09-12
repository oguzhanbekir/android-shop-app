package com.example.eren.myapplication.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eren.myapplication.Models.Order;
import com.example.eren.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class OrdersListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Order> mOrders;
    private Map<String,Order> mapOrders;

    public OrdersListAdapter(Context mContext, ArrayList<Order> mOrders, Map<String,Order> mapOrders) {
        this.mContext = mContext;
        this.mOrders = mOrders;
        this.mapOrders=mapOrders;
    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = View.inflate(mContext,R.layout.settings_orderlist,null);
        TextView tv_createdAt = v.findViewById(R.id.tv_createdAt);
        TextView tv_shopUnitName = v.findViewById(R.id.tv_shopUnitName);
        TextView tv_totalAmount = v.findViewById(R.id.tv_totalAmount);
        TextView tv_paymentType = v.findViewById(R.id.tv_paymentType);
        TextView tv_orderStatus = v.findViewById(R.id.tv_orderStatus);


        tv_createdAt.setText(mOrders.get(position).getCreated_at());
        tv_shopUnitName.setText(mOrders.get(position).getShopUnit());
        tv_totalAmount.setText(Integer.toString(mOrders.get(position).getTotal_amount())+" ₺");
        tv_paymentType.setText(mOrders.get(position).getPayment_type());
        tv_orderStatus.setText(mOrders.get(position).getOrder_status());

        v.setTag(R.id.orderId,mOrders.get(position).getId());

        return v;
    }
}
