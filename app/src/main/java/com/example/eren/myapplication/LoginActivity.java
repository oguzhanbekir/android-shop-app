package com.example.eren.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Helper.JWTUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public RequestQueue mQueue;
    private EditText et_username,et_password;
    private static final String TAG = LoginActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView btn_login_register =  findViewById(R.id.btn_login_register);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        et_username = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        sharedPreferences= getApplicationContext().getSharedPreferences("userinformation", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("userid","")!=""){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        mQueue = Volley.newRequestQueue(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( et_username.getText().toString().trim().equals("") || et_password.getText().toString().trim().equals("") )
                {
                    Toast.makeText(getApplicationContext(), "Lütfen Gerekli Alanları Doldurunuz", Toast.LENGTH_SHORT).show();

                } else {
                    login(et_username.getText().toString(),et_password.getText().toString());
                }
            }
        });

        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);

            }
        });
    }


    private void login(final String email, final String password)
    {
        String url = "http://10.0.3.2:3000/user/login";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    String token = obj.getString("token"); //Token localstorage a yazılacak
                    String userid=JWTUtils.decoded(token);

                    prefEditor = sharedPreferences.edit();
                    prefEditor.putString("userid",userid);
                    prefEditor.apply();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Giriş Başarısız", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("email", email); //Add the data you'd like to send to the server.
                MyData.put("password", password);
                return MyData;
            }
        };
        mQueue.add(MyStringRequest);
    }
}
