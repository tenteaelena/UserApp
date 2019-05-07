package com.example.tente.userappfinalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by tente on 2/4/2018.
 */

public class Preferinte extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("TEST","in preferinte");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_screen);

    }

}
