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
}
