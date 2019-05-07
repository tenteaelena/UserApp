package com.example.tente.userappfinalproject;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by tente on 2/5/2018.
 */

public class MyClient {
    private Uri uri = Uri.parse("content://com.MyProvider.DATA");
    private static ContentProviderClient provider;

    public MyClient(Context context){
        if(provider == null){
            provider = context.getContentResolver().acquireUnstableContentProviderClient(uri);
            Log.d("TEST","Provider Client : "+provider);
        }

    }

    static String[] s=null;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {//preia informatii din mapa pt baza de date
        @Override
        public void onReceive(Context context, Intent intent) {
            s = intent.getStringArrayExtra("date-colectate");
        }
    };


    public void sendInfo(String key, String[] data){
        Log.d("TEST","sendInfo");
        Cursor cursor;
        String value = "";
        try{
            if(provider != null){
                cursor = provider.query(Uri.withAppendedPath(uri,key),null,"",data,"");//send data
            }
        } catch (RemoteException e) {
            provider = null;
        }
    }

    public String getValue(String key){


        //Log.d("TEST","value s "+s[0]);

        Cursor cursor;
        String value = "";
        try{
            if(provider != null){
                cursor = provider.query(Uri.withAppendedPath(uri,key),null,"",null,"");
                if(cursor!=null) {
                    cursor.moveToNext();
                    value = cursor.getColumnName(0);
                    Log.d("TEST","Receive from provider: "+value);
                }
            }
        } catch (RemoteException e) {
            provider = null;
        }
        return value;
    }
}
