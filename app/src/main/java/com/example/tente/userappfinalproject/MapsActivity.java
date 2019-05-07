package com.example.tente.userappfinalproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "TEST";
    private FusedLocationProviderClient fusedLocationProviderClient;//providerul de locatie
    private boolean locationPermisionGranted = false, gpsEnabled=false;
    private LocationManager locationManager;
    private AlertDialog.Builder builder;
    double lat,lng;
    String locality,area;//localitate si judet pentru afisarea in texview dupa geocodare
    TextView tv, tvSpeed;

    static String color,transparency;
    static int size;
    static boolean simulation=false;

    Handler myHandler = new Handler();
    MyClient provider;

    Intent colecteazaDate;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {//preia informatii din aplicatia nr 2
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
                //getExtra
                if(intent.hasExtra("seconds"))
                    Log.d("MyService","Seconds: "+intent.getIntExtra("seconds",-1));
            }
        }
    };// myReceiver

    protected void onCreate(Bundle savedInstanceState) {//se apeleaza la crearea activitatii
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);//seteaza continutul activitatii

        getLocationPermission();//ia permisiunile pentru locatie

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        tv=findViewById(R.id.townTv);
        tvSpeed = findViewById(R.id.tvSpeed);

        size = 22;//valoare default
        color=null;//valoare default

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        provider = new MyClient(this);

        myHandler.postDelayed(locationUpdate,15000);

    }//on  create

    @Override
    protected void onStart() {//se apeleaza la pornirea activitatii
        super.onStart();
        registerReceiver(myReceiver,new IntentFilter("myService"));

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))//se verifica daca gps este activ
            getGPSpermission();
    }//on start

    @Override
    protected void onStop() {//se apeleaza la oprirea activitatii
        super.onStop();
        unregisterReceiver(myReceiver);
    }//on stop

    @Override
    protected void onResume() {//se apeleaza la interactiunea utilizatorului cu aplicatia dupa efectuarea onStart()
        super.onResume();

        BroadcastReceiver mReceiver = new BroadcastReceiver() {//preia informatii de la broadcast local
            @Override
            public void onReceive(Context context, Intent intent) {
                color = intent.getStringExtra("color");
                transparency=intent.getStringExtra("transparency");
                size = intent.getIntExtra("font-size",size);
                simulation = intent.getBooleanExtra("simulation",simulation);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("PREFERINTELE"));

        if(color!=null)
            tv.setTextColor(Color.parseColor("#"+color));//se modifica culoarea  textview
        if(transparency!=null){
            GradientDrawable bgShape = (GradientDrawable)tv.getBackground();
            bgShape.setColor(Color.parseColor("#"+transparency));//se modifica backgroun textview
        }
        tv.setTextSize((float)size);//se modifica marimea fontului din textview


    }//on resume

    public void initMap() {//initializare harta
        Log.d(TAG, "init map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    Runnable locationUpdate = new Runnable() {//location update
        @Override
        public void run() {
            Log.d("TEST","LOCATION UPDATE");
            myHandler.postDelayed(locationUpdate,15000);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                getDeviceLocation();
                Date timestamp = new Date();
                String[]  s ={
                        ""+lat,
                        ""+lng,
                        timestamp.toString(),
                        "0"
                };
                Log.d("TEST","LOCATION UPDATE3");

                provider.sendInfo("collected",s);
            }else
                Log.d(TAG,"no gps runnable");
            Log.d("TEST","Updated location"+lat+" "+lng);
        }
    };


    private void getDeviceLocation() {//locatia curenta a dispozitivului
        Log.d(TAG, "getDeviceLocation");

        try {
            if (locationPermisionGranted) {
                Log.d(TAG,"-----------------"+fusedLocationProviderClient.getLastLocation());
                if(fusedLocationProviderClient.getLastLocation()!=null) {
                    Log.d(TAG,"location NOT null");
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location){
                            if (location != null) {
                                Log.d(TAG, "location found");
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            } else {

                                    getGPSpermission();
                                    Log.d(TAG, "location not found");
                                    Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_LONG).show();
                                    lng = 24.8780174;
                                    lat = 44.8854818;
                            }
                        }
                    });
                }
                else{

                    Log.d(TAG,"location null");
                    Toast.makeText(this, "NULL LOCATION", Toast.LENGTH_SHORT).show();

                        lng = 24.8780174;
                        lat = 44.8854818;

                }
            }
            else{
                Log.d(TAG,"LOCATION NOT GRANTED");
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDevice location: security exception " + e);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {//se apeleaza cand mapas-a incarcat
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
    }

    public void addMarker(double lati, double lngi){//adauga marker pe
        Log.d(TAG,"addMarker");
        LatLng myLocation = new LatLng(lat,lng);
        revGeocode(lati,lngi);
        mMap.addMarker(new MarkerOptions().position(myLocation).title(locality));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        Log.d(TAG,"addMarker finish");
    }

    public void revGeocode(double lat,double lng){//transfrorma lat si ling in localitate si judet si afiseaza in textview
        Log.d(TAG,"revGeocode");
        Geocoder geoCoder = new Geocoder(MapsActivity.this);
        try {
            List<Address> matches = geoCoder.getFromLocation(lat, lng, 1);
            Address adress=matches.get(0);
            locality=adress.getLocality();
            area=adress.getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.setText(locality+", "+area);
    }

    private void getGPSpermission(){//afiseaza un  dialog care cere ca gps-ul sa fie pornit
        Log.d(TAG,"getGpsPermission");
        builder = new AlertDialog.Builder(this);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            builder.setTitle("Gps");
            builder.setMessage("Please enable your gps");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void getLocationPermission(){//ia permisiunile de locatie
        Log.d(TAG,"get location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                    ==PackageManager.PERMISSION_GRANTED){
                locationPermisionGranted = true;
                initMap();
            } else{
                Log.d(TAG,"requestPermission");
                ActivityCompat.requestPermissions(this,permissions,1);//apeleaza on request permission listener
            }
        } else{
                Log.d(TAG,"requestPermission");
                ActivityCompat.requestPermissions(this,permissions,1);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//se apeleaza la request permissions
        Log.d(TAG,"onRequestPermissionResult");
        locationPermisionGranted=false;
        switch (requestCode){
            case 1:
                if (grantResults.length>0 ){
                    for (int i=0;i<grantResults.length;i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermisionGranted = false;
                            Log.d(TAG,"onRequestPermissionResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionResult: persmission granted");
                    locationPermisionGranted=true;
                    initMap();
                }
        }
    }

    public void centerOnLastPosition(View view) {//centreaza pe ultima locatie a dispozitivului
        Log.d(TAG,"Center on last postion");
        if (locationPermisionGranted) {
            getDeviceLocation();
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addMarker(lat, lng);
                }
            },600);

        }

    }

    //listview
    public void moreList(View view) {
        startActivity(new Intent(this,ListActivity.class));
    }

}
