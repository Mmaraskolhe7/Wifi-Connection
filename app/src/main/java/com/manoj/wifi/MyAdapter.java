package com.manoj.wifi;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<String> {




    public MyAdapter( Context context, List<String> objects) {
        super(context,R.layout.list_item, objects);
        }





    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.list_item,parent,false);
        String wifiSSID = getItem(position);
        TextView textView = customView.findViewById(R.id.wifiname);
        ImageView  imageView = customView.findViewById(R.id.strength);
        textView.setText(wifiSSID);
        if(MainActivity.flag)
        switch (MainActivity.level){
            case 1:
                imageView.setImageResource(R.drawable.s1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.s2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.s3);
                break;
        }
        else {
            switch (MainActivity.level) {
                case 1:
                    imageView.setImageResource(R.drawable.os1);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.os2);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.os3);
                    break;
            }
        }
return customView;
    }
}
