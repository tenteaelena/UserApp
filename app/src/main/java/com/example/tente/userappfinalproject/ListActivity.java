package com.example.tente.userappfinalproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class ListActivity extends Activity {

    ListView lv;
    private ArrayAdapter<String> adapter;
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        items = new ArrayList<String>();
        items.add("Statistics");
        items.add("Max speed");
        items.add("Toogle all trail on/off");
        items.add("Settings");

        lv = findViewById(R.id.optLv);
        adapter = new GroupListAdapter(this,R.layout.list_item,items);

        lv.setAdapter(adapter);

    }


    private class GroupListAdapter extends ArrayAdapter<String>
    {
        public GroupListAdapter(Context context, int resource, List<String> objects){
            super(context,resource,objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view=convertView;
            if(view == null ){
                view = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }
            final Button btn=view.findViewById(R.id.btnItem);//gaseste butonul din list_item
            final String p=getItem(position);

            btn.setText(p);//seteaza textul butonului

            btn.setOnClickListener(new View.OnClickListener() { //event
                @Override
                public void onClick(View view) {
                    Log.d("TEST","in preferinte activity");
                    Intent in = new Intent(getContext(),OptiuniActivity.class);
                    in.putExtra("pozitie",position);//trimite ce buton a fost apasat
                    startActivity(in);//porneste activitatea
                }
            });
            return view;
        }
    }


}
