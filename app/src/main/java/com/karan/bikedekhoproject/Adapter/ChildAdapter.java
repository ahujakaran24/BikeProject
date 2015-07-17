package com.karan.bikedekhoproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karan.bikedekhoproject.FilterActivity;
import com.karan.bikedekhoproject.Interfaces.ChangeSelectionListener;
import com.karan.bikedekhoproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by karanahuja on 16/07/15.
 */
public class ChildAdapter extends BaseAdapter {

    Context context;
    JSONArray data;
    LayoutInflater inflater;
    ArrayList<String> selected;
    String key;
    ChangeSelectionListener changeSelectionListener;

    public ChildAdapter(Context context, JSONArray data, String key)
    {
        this.context = context;
        this.data = data;
        this.key = key;
        changeSelectionListener = (ChangeSelectionListener)context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length();
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        View v = convertView;

        if(v==null) {
            v = inflater.inflate(R.layout.child, null);
        }

        TextView text = (TextView)v.findViewById(R.id.content);
        final CheckBox check = (CheckBox)v.findViewById(R.id.check);

       if(changeSelectionListener.getCheckedItems(key)!=null && changeSelectionListener.getCheckedItems(key).size()!=0)
       {
           /*This bit ensures user will see his/her checks all the time*/
           ArrayList<String> tempCheckedItems = changeSelectionListener.getCheckedItems(key);
           for(int i = 0 ; i < tempCheckedItems.size(); i ++)
           {
               if(Integer.valueOf(tempCheckedItems.get(i))==position)
               {
                   check.setChecked(true);
               }
           }
       }

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    try {
                        if(key.equals("sort")&&changeSelectionListener.getCheckedItems("sort").size() == 1)
                        {
                            Toast.makeText(context,"Can't select multiple sorting methods",Toast.LENGTH_LONG).show();
                            buttonView.setChecked(false);
                        }else {
                            changeSelectionListener.addSelectedItem(key, data.getJSONObject(position).getString("link_rewrite"));
                            changeSelectionListener.addCheckedItem(key, String.valueOf(position));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        changeSelectionListener.removeUnselectedItem(key, data.getJSONObject(position).getString("link_rewrite"));
                        changeSelectionListener.removeCheckedItem(key,String.valueOf(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            text.setText(data.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
            text.setText("?????");
        }

        return v;
    }

    @Override
    public int getViewTypeCount() { return getCount(); }

    @Override
    public  int getItemViewType(int position) {return position;}
}

