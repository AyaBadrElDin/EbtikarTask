package com.av.ebtikartask.networkUtilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RequestData {


    public  static void getData(Context context, String apiLink, final GetCallback.onRequestData onRequestDataCallback){

         RequestQueue requestQueue = Volley.newRequestQueue(context);
         JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,apiLink,
                 null, new  com.android.volley. Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {

                 try {
                     JSONArray jsonArray = response.getJSONArray("clients");
                     onRequestDataCallback.onSuccess(jsonArray);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
         }, new  com.android.volley.Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 onRequestDataCallback.onFailure(error.getMessage());
             }
         });
         requestQueue.add(req);


     }
}
