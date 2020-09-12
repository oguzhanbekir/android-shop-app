package com.example.eren.myapplication.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eren.myapplication.Helper.FragmentNavigationManager;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.R;

import com.example.eren.myapplication.Adapter.BasketProductsRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBasket extends Fragment implements IOnBackPressed{


    private Button btn_showaddress;
    private NavigationManager navigationManager;


    public FragmentBasket() {
        // Required empty public constructor
    }
    public static FragmentBasket newInstance(String param1) {
        FragmentBasket fragment = new FragmentBasket();
     //   Bundle args = new Bundle();
       // args.putString(KEY_TITLE, param1);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navigationManager= MainActivity.navigationManager;

        LinearLayoutManager layoutManager=new LinearLayoutManager(inflater.getContext(),LinearLayoutManager.VERTICAL,false);
        View view = inflater.inflate(R.layout.fragment_fragment_basket, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.recyclerView_basket_Products);
        recyclerView.setLayoutManager(layoutManager);
        BasketProductsRecyclerViewAdapter adapter=new BasketProductsRecyclerViewAdapter(inflater.getContext());
        recyclerView.setAdapter(adapter);

        btn_showaddress=view.findViewById(R.id.btn_basket_address_list);
        btn_showaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationManager.showFragment(FragmentAddresses.newInstance(""),false);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

}
