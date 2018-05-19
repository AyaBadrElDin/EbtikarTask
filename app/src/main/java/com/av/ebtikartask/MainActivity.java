package com.av.ebtikartask;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.av.ebtikartask.Model.Clients;
import com.av.ebtikartask.Model.ClientsAdapter;
import com.av.ebtikartask.PersistsData.StoreData;
import com.av.ebtikartask.databinding.ActivityMainBinding;
import com.av.ebtikartask.networkUtilities.RequestData;
import com.av.ebtikartask.networkUtilities.GetCallback;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {


    private ActivityMainBinding activityMainBinding;
    private ClientsAdapter clientsAdapter;

    private static final int STORAGE_PERMISSION_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    public static final int STORAGE_PERMISSION_CODE_CALL_PHONE = 102;

    private final static int REQUEST_CHECK_SETTINGS_GPS=103;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=104;

    private Location myLocation;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        Setup_UI();

    }

    private void Setup_UI(){

        activityMainBinding.rvClientsList.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.rvClientsList.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityMainBinding.rvClientsList.getContext(),
                DividerItemDecoration.VERTICAL);
        activityMainBinding.rvClientsList.addItemDecoration(dividerItemDecoration);

        setUpGClient();

        String apiLink = new StoreData(MainActivity.this).loadApiLink();
        if(!apiLink.equals("")) {
            requestDataFromApi(apiLink);
        }





    }
    private boolean requestPermissionCamera() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE_CAMERA);

            return false;
        }else{
            scanQRCode();
            return  true;
        }


    }

    private boolean requestPermissionLocation() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ID_MULTIPLE_PERMISSIONS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            return false;
        } else {
            getMyLocation();
            return  true;
        }






    }


    private synchronized void setUpGClient()
    {

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
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
            new StoreData(this).saveApiLink(result);
            requestDataFromApi(result);

        }else if(requestCode==REQUEST_CHECK_SETTINGS_GPS){
            if(resultCode==Activity.RESULT_OK){
                getMyLocation();
           }else {
                requestPermissionLocation();
            }

        }
    }

    private void requestDataFromApi(String result) {

        showProgressBar();
        RequestData.getData(MainActivity.this,result, new GetCallback.onRequestData() {
            @Override
            public void onSuccess(ArrayList<Clients> clientsArrayList) {
                hideProgressBar();
                clientsAdapter = new ClientsAdapter(MainActivity.this,clientsArrayList);
                activityMainBinding.rvClientsList.setAdapter(clientsAdapter);
                clientsAdapter.notifyDataSetChanged();



            }

            @Override
            public void onFailure(String error) {
                hideProgressBar();
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }

   @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //If permission is granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //     Toast.makeText(this, "Permission granted now you can select img or video", Toast.LENGTH_LONG).show();
               if(requestCode == REQUEST_CODE_QR_SCAN||requestCode==STORAGE_PERMISSION_CODE_CAMERA)
                  scanQRCode();


            int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED){

                   getMyLocation();

            }

        } else {

            if(requestCode == REQUEST_CODE_QR_SCAN||requestCode==STORAGE_PERMISSION_CODE_CAMERA)
            requestPermissionCamera();

            if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS)
                requestPermissionLocation();



        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
      requestPermissionLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        if (myLocation != null) {
            Double latitude=myLocation.getLatitude();
            Double longitude=myLocation.getLongitude();

            new StoreData(this).saveUserLatitude(String.valueOf(latitude));
            new StoreData(this).saveUserLongitude(String.valueOf(longitude));

            try {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> listAddresses = null;

                listAddresses = geocoder.getFromLocation(latitude, longitude, 1);

                if(null!=listAddresses&&listAddresses.size()>0) {


                    String country = listAddresses.get(0).getCountryName(); //country
                    String city = listAddresses.get(0).getAdminArea();//city
                    String region = listAddresses.get(0).getSubAdminArea();//region

                    String fullUserAddress = country +","+city+","+region;
                    new StoreData(this).saveUserAddress(fullUserAddress);


                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    private void getMyLocation(){
        showProgressBar();
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected())
            {
                int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    myLocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED)
                                    {
                                        myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                        hideProgressBar();
                                        if(new StoreData(MainActivity.this).loadApiLink().equals(""))
                                        requestPermissionCamera();
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try
                                    {
                                        status.startResolutionForResult(MainActivity.this,REQUEST_CHECK_SETTINGS_GPS);
                                    }
                                    catch (IntentSender.SendIntentException e)
                                    {
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    private void showProgressBar(){
        activityMainBinding.pbLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        activityMainBinding.pbLoading.setVisibility(View.INVISIBLE);

    }

}
