package com.av.ebtikartask.PersistsData;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.av.ebtikartask.R;

import java.util.Date;

/**
 * Created by Maiada on 5/16/2018.
 */

public class CallReceiver extends PhonecallReceiver {



    @Override
    protected void onIncomingCallStarted(Context context,String number, Date start) {
        showAlertDialog(context,number);
    }



    @Override
    protected void onOutgoingCallStarted(Context context,String number, Date start) {
        showAlertDialog(context,number);

    }

    @Override
    protected void onIncomingCallEnded(Context context,String number, Date start, Date end) {
        showAlertDialog(context,number);

    }

    @Override
    protected void onOutgoingCallEnded(Context context,String number, Date start, Date end) {
        showAlertDialog(context,number);

    }

    @Override
    protected void onMissedCall(Context context,String number, Date start) {
        showAlertDialog(context,number);

    }

    private void showAlertDialog(Context context,String phoneNumber) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View overLayout   = layoutInflater.inflate(R.layout.over_layout,null);

        TextView textViewUserName    =  overLayout.findViewById(R.id.tv_user_name);
        TextView textViewPhoneNumber =  overLayout.findViewById(R.id.tv_number);
        TextView textViewUserLatLng =  overLayout.findViewById(R.id.tv_use_latlng);
        TextView textViewUserAddress =  overLayout.findViewById(R.id.tv_user_address);


        textViewUserName.setText(new StoreData(context).loadUserToCall());
        textViewPhoneNumber.setText(phoneNumber);
        textViewUserLatLng.setText(new StoreData(context).loadUserLatitude()+","+new StoreData(context).loadUserLongitude());
        textViewUserAddress.setText(new StoreData(context).loadUserAddress());

        AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.Theme_AppCompat_Dialog_Alert);
        alert.setView(overLayout);


        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }



        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setType(LAYOUT_FLAG);
        alertDialog.show();
    }
}