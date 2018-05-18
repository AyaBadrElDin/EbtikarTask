package com.av.ebtikartask.networkUtilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.av.ebtikartask.Model.Clients;
import com.av.ebtikartask.Model.Interest;
import com.av.ebtikartask.Model.Language;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RequestData {


    public  static void getData(Context context, String apiLink, final GetCallback.onRequestData onRequestDataCallback){

         RequestQueue requestQueue = Volley.newRequestQueue(context);
         JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,apiLink,
                 null, new  com.android.volley. Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {

                 try {
                     ArrayList<Clients> clientsList = new ArrayList<>();
                     JSONArray clientsJsonArray = response.getJSONArray("clients");
                     if(clientsJsonArray.length()!=0){
                         for(int c=0;c<clientsJsonArray.length();c++){
                             JSONObject clientsJsonObject = clientsJsonArray.getJSONObject(c);
                             Clients clients = new Clients();
                             clients.setName(clientsJsonObject.getString("name"));
                             clients.setAge(clientsJsonObject.getString("age"));
                             clients.setMobile(clientsJsonObject.getString("mobile"));

                             ArrayList<Interest>  interestsList = new ArrayList<>();
                             JSONArray interestsJsonArray = clientsJsonObject.getJSONArray("interests");
                             for(int i=0;i<interestsJsonArray.length();i++){
                                 JSONObject interestsJsonObject = interestsJsonArray.getJSONObject(i);
                                 Interest interest = new Interest();
                                 interest.setTitle(interestsJsonObject.getString("title"));
                                 interestsList.add(interest);
                             }
                             clients.setInterestList(interestsList);

                             ArrayList<Language>  languagesList = new ArrayList<>();
                             JSONArray languagesJsonArray = clientsJsonObject.getJSONArray("languages");
                             for(int l=0;l<languagesJsonArray.length();l++){
                                 JSONObject languagesJsonObject = languagesJsonArray.getJSONObject(l);
                                 Language language = new Language();
                                 language.setTitle(languagesJsonObject.getString("title"));
                                 language.setLevel(languagesJsonObject.getString("level"));
                                 languagesList.add(language);
                             }
                             clients.setLanguageList(languagesList);
                             clientsList.add(clients);


                         }
                     }


                     onRequestDataCallback.onSuccess(clientsList);

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
