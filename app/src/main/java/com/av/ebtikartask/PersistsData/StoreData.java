package com.av.ebtikartask.PersistsData;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Maiada on 5/18/2018.
 */

public class StoreData {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String DATABASE_NAME = "com.av.ebtikartask";


    public StoreData(Context context) {
        super();
        sharedPreferences = context.getSharedPreferences(DATABASE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveApiLink(String apiLink) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ApiLink", apiLink);
        editor.commit();
    }

    public String loadApiLink() {
        String apiLink = sharedPreferences.getString("ApiLink", "");
        return apiLink;
    }


    public void saveUserToCall(String userToCall) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserToCall", userToCall);
        editor.commit();
    }

    public String loadUserToCall() {
        String userToCall = sharedPreferences.getString("UserToCall", "");
        return userToCall;
    }

    public void saveUserAddress(String userAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserAddress", userAddress);
        editor.commit();
    }

    public String loadUserAddress() {
        String userAddress = sharedPreferences.getString("UserAddress", "");
        return userAddress;
    }


    public void saveUserLongitude(String userLongitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserLongitude", userLongitude);
        editor.commit();
    }

    public String loadUserLongitude() {
        String userLongitude = sharedPreferences.getString("UserLongitude", "");
        return userLongitude;
    }


    public void saveUserLatitude(String userLatitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserLatitude", userLatitude);
        editor.commit();
    }

    public String loadUserLatitude() {
        String userLatitude = sharedPreferences.getString("UserLatitude", "");
        return userLatitude;
    }


}
