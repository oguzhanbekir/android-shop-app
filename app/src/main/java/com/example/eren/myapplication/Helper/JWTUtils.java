package com.example.eren.myapplication.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            JSONObject obj = new JSONObject(getJson(split[1]));
            String userId = obj.getString("userId");
            String email = obj.getString("email");
            String phone = obj.getString("phone");
            String userType = obj.getString("userType");

            return userId;
        } catch (UnsupportedEncodingException e) {
            //Error
            return "";
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}