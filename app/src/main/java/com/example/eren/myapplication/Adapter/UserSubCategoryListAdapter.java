package com.example.eren.myapplication.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eren.myapplication.R;

import java.util.ArrayList;

public class UserSubCategoryListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mSubName;
    private ArrayList<Drawable>  mVector;

    public UserSubCategoryListAdapter(Context mContext, ArrayList<String> mSubName, ArrayList<Drawable> mVector) {
        this.mContext = mContext;
        this.mSubName = mSubName;
        this.mVector = mVector;
    }

    @Override
    public int getCount() {
        return mSubName.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.my_account_sub_category,null);
        TextView tv_Name = view.findViewById(R.id.tv_accountName);
        ImageView img_vector = view.findViewById(R.id.img_vector);

        tv_Name.setText(mSubName.get(position));
        img_vector.setImageDrawable(mVector.get(position));
        return view;
    }
}
