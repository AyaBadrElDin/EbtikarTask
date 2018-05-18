package com.av.ebtikartask.networkUtilities;


import org.json.JSONArray;

public abstract class GetCallback {


    public interface onRequestData{

        void onSuccess(JSONArray jsonArray);
        void onFailure(String error);
    }


}
