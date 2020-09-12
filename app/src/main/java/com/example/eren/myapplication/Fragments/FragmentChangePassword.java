package com.example.eren.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.MainActivity;
import com.example.eren.myapplication.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static java.security.AccessController.getContext;


public class FragmentChangePassword extends Fragment implements IOnBackPressed {
    private EditText et_changePassword,et_changePasswordCorrect;
    private Button btn_updatePassword;

    public FragmentChangePassword() {
        // Required empty public constructor
    }
    public static FragmentChangePassword newInstance(String param1) {
        FragmentChangePassword fragment = new FragmentChangePassword();
        //   Bundle args = new Bundle();
        // args.putString(KEY_TITLE, param1);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_change_password, container, false);

        et_changePassword = view.findViewById(R.id.et_changepassword);
        et_changePasswordCorrect = view.findViewById(R.id.et_changepasswordCorrect);
        btn_updatePassword = view.findViewById(R.id.btn_updatePassword);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        btn_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_changePassword.getText().toString().trim().equals("") || et_changePasswordCorrect.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(),"Lütfen Tüm Alanları Doldurunuz",Toast.LENGTH_SHORT).show();
                } else {
                    if(et_changePassword.getText().toString().equals(et_changePasswordCorrect.getText().toString())){
                        updatePassword(et_changePassword.getText().toString(),"5c213f959babca41806c7a01");
                        Toast.makeText(getContext(), "Şifre Değiştirildi", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Şifreler Birbirinden Farklı",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void updatePassword(final String password,final String userId){
        OkHttpClient clientUserAddress = new OkHttpClient();

        MediaType mediaType2 = MediaType.parse("application/json");
        RequestBody body2 = RequestBody.create(mediaType2, "[{\"propName\": \"password\",\"value\":\""+password+"\"}]");
        okhttp3.Request request2 = new okhttp3.Request.Builder()
                .url("http://10.0.3.2:3000/user/"+userId)
                .patch(body2)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            okhttp3.Response response2 = clientUserAddress.newCall(request2).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            navigationManager= MainActivity.navigationManager;

            navigationManager.showFragment(FragmentSettings.newInstance(""),false);

        }

    }

    public NavigationManager navigationManager;
    @Override
    public boolean onBackPressed() {
        navigationManager= MainActivity.navigationManager;

        navigationManager.showFragment(FragmentSettings.newInstance(""),false);

        return true;
    }
}
