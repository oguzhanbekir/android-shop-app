package com.example.eren.myapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eren.myapplication.Models.User;
import com.example.eren.myapplication.R;

import java.util.ArrayList;

public class UserTopInfoListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> mUser;

    public UserTopInfoListAdapter(Context mContext, ArrayList<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return mUser.size();
    }

    @Override
    public Object getItem(int position) {
        return mUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.my_account_top_info,null);
        TextView tv_accountMail = view.findViewById(R.id.tv_accountEmail);
        TextView tv_accountPhone = view.findViewById(R.id.tv_accountPhone);

        tv_accountMail.setText(mUser.get(position).getEmail());
        tv_accountPhone.setText(mUser.get(position).getPhone());

        view.setTag(R.id.accountUserId,mUser.get(position).getId());
        return view;
    }
}
