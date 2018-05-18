package com.av.ebtikartask;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.av.ebtikartask.Model.Clients;
import com.av.ebtikartask.Model.ClientsAdapter;
import com.av.ebtikartask.databinding.ActivityMainBinding;
import com.av.ebtikartask.networkUtilities.RequestData;
import com.av.ebtikartask.networkUtilities.GetCallback;
import com.blikoon.qrcodescanner.QrCodeActivity;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding activityMainBinding;
    private ClientsAdapter clientsAdapter;

    private static final int STORAGE_PERMISSION_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_QR_SCAN = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        activityMainBinding.rvClientsList.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.rvClientsList.setHasFixedSize(true);


        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE_CAMERA);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            scanQRCode();
        }


    }

    private void scanQRCode() {
        Intent i = new Intent(MainActivity.this,QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            requestDataFromApi(result);


        }
    }

    private void requestDataFromApi(String result) {


        RequestData.getData(MainActivity.this,result, new GetCallback.onRequestData() {
            @Override
            public void onSuccess(ArrayList<Clients> clientsArrayList) {

                clientsAdapter = new ClientsAdapter(MainActivity.this,clientsArrayList);
                activityMainBinding.rvClientsList.setAdapter(clientsAdapter);
                clientsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(String error) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //If permission is granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //     Toast.makeText(this, "Permission granted now you can select img or video", Toast.LENGTH_LONG).show();
            scanQRCode();
        } else {

            requestCameraPermission();
        }
    }


}
