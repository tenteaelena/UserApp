package com.example.tente.userappfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OptiuniActivity extends AppCompatActivity {


    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TEST","in preferinte activity");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        int opt = bundle.getInt("pozitie");
        Log.d("TEST","optiune in preferinte = "+opt);

        if(opt==3) {
            if (getFragmentManager().findFragmentById(android.R.id.content) == null)
                getFragmentManager().beginTransaction().add(android.R.id.content, new Preferinte()).commit();//afiseaza prefference screen
        }
        else {
            Log.d("TEST", "Not yet implemented");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {//listener pentru schimbarea preferintei
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("TEST","Settings key changed: " + key);

                String culori[]={//valori culori luate din fisierul colors
                        Integer.toHexString(ContextCompat.getColor(OptiuniActivity.this, R.color.textColorWhite)),
                        Integer.toHexString(ContextCompat.getColor(OptiuniActivity.this, R.color.textColorRed)),
                        Integer.toHexString(ContextCompat.getColor(OptiuniActivity.this, R.color.textColorBlue)),
                        Integer.toHexString(ContextCompat.getColor(OptiuniActivity.this, R.color.textColorBlack))
                };
                String transparenta[]={//valori transparenta luate din fisierul colors
                    Integer.toHexString(ContextCompat.getColor(OptiuniActivity.this, R.color.shapeTransparency0)),
                    Integer.toHexString(ContextCompat.getColor(OptiuniActivity.this, R.color.shapeTransparency50))
                };
                int size[]={19,20,21,22};//valori pentru marime

                Intent intent = new Intent("PREFERINTELE");
                String s="";//preferinta

                if(key.equals("pref_color")) {//preferinta este culoare

                    s = PreferenceManager.getDefaultSharedPreferences(OptiuniActivity.this).getString(key, null);//ia valoarea curenta/selectata
                    intent.putExtra("color",culori[Integer.parseInt(s)]);//adauga culoarea ca extra
                    LocalBroadcastManager.getInstance(OptiuniActivity.this).sendBroadcast(intent);//trimite extra

                }else if(key.equals("pref_transparency")){//preferinta este  transparenta

                    s = PreferenceManager.getDefaultSharedPreferences(OptiuniActivity.this).getString(key, null);
                    intent.putExtra("transparency",transparenta[Integer.parseInt(s)]);
                    LocalBroadcastManager.getInstance(OptiuniActivity.this).sendBroadcast(intent);

                }
                else if(key.equals("pref_font_size")){

                    s = PreferenceManager.getDefaultSharedPreferences(OptiuniActivity.this).getString(key, null);
                    intent.putExtra("font-size",size[Integer.parseInt(s)]);
                    LocalBroadcastManager.getInstance(OptiuniActivity.this).sendBroadcast(intent);

                }
                else if(key.equals("pref_simulation")){

                    boolean ok = PreferenceManager.getDefaultSharedPreferences(OptiuniActivity.this).getBoolean(key,false);
                    intent.putExtra("simulation",ok);
                    LocalBroadcastManager.getInstance(OptiuniActivity.this).sendBroadcast(intent);

                }

            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);//inregistreaza listener pentru schimbarea preferintei
    }
}
