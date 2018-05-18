package com.av.ebtikartask.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.av.ebtikartask.R;

/**
 * Created by Maiada on 5/18/2018.
 */

public class ClientsHolder extends RecyclerView.ViewHolder {

    TextView userName,userAge;
    LinearLayout linearLayoutInterests,linearLayoutLanguages;

    public ClientsHolder(View itemView) {
        super(itemView);
        userName               = itemView.findViewById(R.id.tv_user_name);
        userAge                = itemView.findViewById(R.id.tv_user_age);
        linearLayoutInterests  = itemView.findViewById(R.id.linear_interests);
        linearLayoutLanguages  = itemView.findViewById(R.id.linear_languages);


    }
}
