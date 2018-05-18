package com.av.ebtikartask.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.av.ebtikartask.R;

import java.util.ArrayList;
import java.util.List;

import static com.av.ebtikartask.MainActivity.STORAGE_PERMISSION_CODE_CALL_PHONE;

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
    public void onBindViewHolder(final ClientsHolder holder, int position) {

        final Clients clients = clientsList.get(position);

        holder.userName.setText(clients.getName());
        holder.userAge.setText(clients.getAge());

        List<Interest> interests = clients.getInterestList();
        for(int i=0;i<interests.size();i++){
            Interest  interest = clients.getInterestList().get(i);
            TextView textView = new TextView(activity);
            if(i==interests.size()-1)
               textView.setText(interest.getTitle()+".");
            else
               textView.setText(interest.getTitle()+" , ");

            holder.linearLayoutInterests.addView(textView);
        }

        List<Language> languages = clients.getLanguageList();
        for(int i=0;i<languages.size();i++){
            Language  language = clients.getLanguageList().get(i);
            TextView textView = new TextView(activity);
            textView.setPadding(0,0,15,0);
            textView.setText(language.getTitle()+" , "+ language.getLevel()+".");
            holder.linearLayoutLanguages.addView(textView);
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions()){
                       String mobileNumber = clients.getMobile();
                       activity.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+mobileNumber)));
                }else {
                    Toast.makeText(activity,"يجب السماح لإجراء اتصال  ", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }


    private boolean checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE_CALL_PHONE);
            }
            return false;
        }else{
            return  true;
        }

    }

}

