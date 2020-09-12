package com.example.eren.myapplication.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eren.myapplication.Models.Addresses;
import com.example.eren.myapplication.Models.City;
import com.example.eren.myapplication.R;

import java.util.ArrayList;
import java.util.Map;

public class PaymentListAdapter extends BaseAdapter {


    private Context mContext;

    public int mSelectedItem=0;
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<Drawable> icons=new ArrayList<>();

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public PaymentListAdapter(Context mContext, ArrayList<String> titles, ArrayList<Drawable> icons) {
        this.mContext = mContext;
        this.titles=titles;
        this.icons=icons;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.layout_payment_listitem,null);
        TextView tvName = (TextView)v.findViewById(R.id.payment_title);
        ImageView imgIcon = v.findViewById(R.id.payment_simge);
        LinearLayout linearLayout=v.findViewById(R.id.payment_linearlayout);

        tvName.setText(titles.get(position));
        imgIcon.setBackground(icons.get(position));

        if (position == mSelectedItem) {

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.background_products) );
            } else {
                linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_products));
            }
        }


        return v;
    }
}
