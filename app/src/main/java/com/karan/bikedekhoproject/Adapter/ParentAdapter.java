package com.karan.bikedekhoproject.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karan.bikedekhoproject.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.zip.Inflater;

/**
 * Created by karanahuja on 16/07/15.
 */
public class ParentAdapter extends BaseAdapter {

    Context context;
    String[] data;
    LayoutInflater inflater;
    int selectedPosition;

    public ParentAdapter(Context context, String[] data, int selectedPosition)
    {
        this.context = context;
        this.data = data;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedPosition=selectedPosition;
    }


    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v==null) {
            v = inflater.inflate(R.layout.parent, null);
        }

        TextView text = (TextView)v.findViewById(R.id.content);
        text.setText(data[position]);
        text.setTextColor(R.color.black);

         LinearLayout wrapper = (LinearLayout)v.findViewById(R.id.wrapper);


        if(selectedPosition==position)
            wrapper.setBackgroundResource(R.color.white);

        return v;
    }
}
