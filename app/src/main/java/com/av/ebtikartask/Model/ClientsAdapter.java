package com.av.ebtikartask.Model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.av.ebtikartask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maiada on 5/18/2018.
 */

public class ClientsAdapter extends RecyclerView.Adapter<ClientsHolder> {

    ArrayList<Clients>  clientsList;
    Activity activity;

    public ClientsAdapter(Activity activity, ArrayList<Clients>  clientsList) {
        this.activity = activity;
        this.clientsList = clientsList;
    }

    @Override
    public ClientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_data,null);
        return new ClientsHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientsHolder holder, int position) {

        Clients clients = clientsList.get(position);

        holder.userName.setText(clients.getName());
        holder.userAge.setText(clients.getAge());

        List<Interest> interests = clients.getInterestList();
        for(int i=0;i<interests.size();i++){
            Interest  interest = clients.getInterestList().get(i);
            TextView textView = new TextView(activity);
            textView.setPadding(0,0,15,0);
            textView.setText(interest.getTitle());
            holder.linearLayoutInterests.addView(textView);
        }

        List<Language> languages = clients.getLanguageList();
        for(int i=0;i<languages.size();i++){
            Language  language = clients.getLanguageList().get(i);
            TextView textView = new TextView(activity);
            textView.setPadding(0,0,15,0);
            textView.setText(language.getTitle()+"  :  "+ language.getLevel());
            holder.linearLayoutLanguages.addView(textView);
        }




    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }
}
