package com.av.ebtikartask.networkUtilities;


import com.av.ebtikartask.Model.Clients;

import org.json.JSONArray;

import java.util.ArrayList;

public abstract class GetCallback {


    public interface onRequestData{

        void onSuccess(ArrayList<Clients> jsonArray);
        void onFailure(String error);
    }


}
