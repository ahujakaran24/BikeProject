package com.karan.bikedekhoproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.ExecutorDelivery;
import com.karan.bikedekhoproject.Adapter.ChildAdapter;
import com.karan.bikedekhoproject.Adapter.ParentAdapter;
import com.karan.bikedekhoproject.Entity.FilterElements;
import com.karan.bikedekhoproject.Entity.Selection;
import com.karan.bikedekhoproject.Interfaces.ChangeSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FilterActivity extends Activity implements ChangeSelectionListener {

    private String[] parentContents  = {"SORT BY","PRICE","BRAND", "ENGINE", "STYLE", "IGNITION" };
    private ListView parent,child;
    private ParentAdapter parentAdapter;
    private ChildAdapter childAdapter;
    private FilterElements filterElements;
    public JSONObject filter;
    private Button applyFilter;
    private static String sortingMethod;
    public Selection price, brands, engine, style, ignition, sort;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        if(getIntent().getSerializableExtra("price")==null) {
            price = new Selection();
            brands = new Selection();
            engine = new Selection();
            style = new Selection();
            ignition = new Selection();
            sort = new Selection();
        }else{
            price = (Selection)getIntent().getSerializableExtra("price");
            brands = (Selection)getIntent().getSerializableExtra("brands");
            engine = (Selection)getIntent().getSerializableExtra("engine");
            style = (Selection)getIntent().getSerializableExtra("style");
            ignition = (Selection)getIntent().getSerializableExtra("ignition");
            sort = (Selection)getIntent().getSerializableExtra("sort");
        }

        filter = new JSONObject();

        parent = (ListView)findViewById(R.id.parent);
        child = (ListView)findViewById(R.id.child);
        applyFilter = (Button)findViewById(R.id.finish);

        parentAdapter = new ParentAdapter(this,parentContents,-1);
        parent.setAdapter(parentAdapter);
        parent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    parentAdapter = new ParentAdapter(getApplicationContext(), parentContents, position);
                    parent.setAdapter(parentAdapter);

                    try {  /*Load child view also*/
                        loadChildList(position);
                    }catch (Exception e){
                    }
            }
        });

        if(getIntent()!=null) {
            filterElements = (FilterElements) getIntent().getSerializableExtra("filterList");
            try {
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getSort()),"sort");
                child.setAdapter(childAdapter);
            } catch (JSONException e) {
            }
        }


        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Make Filter json object*/
                if(price.getSelected().size()==0&&brands.getSelected().size()==0&&engine.getSelected().size()==0&&style.getSelected().size()==0
                        &&ignition.getSelected().size()==0&&sort.getSelected().size()==0)
                {
                    /*No Check seleted. All Arraylist<> is size = 0*/
                    try {
                        Toast.makeText(getApplicationContext(),"No filters applied",Toast.LENGTH_LONG).show();
                        finish();
                    }catch (Exception e){
                        Log.d("Error ","while making json at NO check");
                    }
                }else{
                    /*Something is selected*/
                    int i;
                    try {
                        if(brands.getSelected().size()!=0) {
                            JSONObject list1 = new JSONObject();
                            for (i = 0; i < brands.getSelected().size(); i++) {
                                list1.put(String.valueOf(i), brands.getSelected().get(i));
                            }
                            filter.put("brand", list1);
                        }

                        if(price.getSelected().size()!=0) {
                            JSONObject list1 = new JSONObject();
                            for (i = 0; i < price.getSelected().size(); i++) {
                                list1.put(String.valueOf(i), price.getSelected().get(i));
                            }
                            filter.put("price", list1);
                        }

                            filter.put("fuelType", new JSONObject().put("0","Petrol")); //fuelType keeping empty for now, since Bike is always petrol
                           // filter.put("city",new JSONObject().put("0","adilabad")); //City is empty in server db

                        if(style.getSelected().size()!=0) {
                            JSONObject list1 = new JSONObject();
                            for (i = 0; i < style.getSelected().size(); i++) {
                                list1.put(String.valueOf(i), style.getSelected().get(i));
                            }
                            filter.put("style", list1);
                        }


                        if(ignition.getSelected().size()!=0) {
                            JSONObject list1 = new JSONObject();
                            for (i = 0; i < ignition.getSelected().size(); i++) {
                                list1.put(String.valueOf(i), ignition.getSelected().get(i));
                            }
                            filter.put("start_option", list1);
                        }

                        if(engine.getSelected().size()!=0) {
                            JSONObject list1 = new JSONObject();
                            for (i = 0; i < engine.getSelected().size(); i++) {
                                list1.put(String.valueOf(i), engine.getSelected().get(i));
                            }
                            filter.put("cc", list1);
                        }

                    }catch (Exception e){
                        Log.d("Error ","while making json at SOME check");
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("filter", filter.toString());
                //Send out the selections for later return use
                intent.putExtra("price",price);
                intent.putExtra("engine",engine);
                intent.putExtra("brands",brands);
                intent.putExtra("style",style);
                intent.putExtra("ignition",ignition);
                intent.putExtra("sort",sort);

                if(sortingMethod!=null) intent.putExtra("sortingMethod", sortingMethod);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public void loadChildList(int selection) throws JSONException
    {
        switch (selection)
        {
            case 0:
                /*Context  ,   JSONArray from server,         key   ******/
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getSort()),"sort");
                break;
            case 1:
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getPrice()),"price");
                break;
            case 2:
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getBrand()),"brands");
                break;
            case 3:
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getEngine()),"engine");
                break;
            case 4:
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getStyle()),"style");
                break;
            case 5:
                childAdapter = new ChildAdapter(this,new JSONArray(filterElements.getIgnition()),"ignition");
                break;
        }
        child.setAdapter(childAdapter);
    }

    @Override
    public void addSelectedItem(String key, String value) {
        switch (key)
        {
            case "price" :
                price.getSelected().add(value);
                break;
            case "brands" :
                brands.getSelected().add(value);
                break;
            case "engine" :
                engine.getSelected().add(value);
                break;
            case "style" :
                style.getSelected().add(value);
                break;
            case "ignition" :
                ignition.getSelected().add(value);
                break;
            case "sort" :
                sort.getSelected().add(value);
                sortingMethod = sort.getSelected().get(0);
                break;
            default:
                Log.d("error","error in key");
                break;
        }

    }

    @Override
    public void removeUnselectedItem(String key, String value) {
        switch (key)
        {
            case "price" :
                price.getSelected().remove(value);
                break;
            case "brands" :
                brands.getSelected().remove(value);
                break;
            case "engine" :
                engine.getSelected().remove(value);
                break;
            case "style" :
                style.getSelected().remove(value);
                break;
            case "ignition" :
                ignition.getSelected().remove(value);
                break;
            case "sort" :
                sort.getSelected().remove(value);
                sortingMethod = null;
                break;
            default:
                Log.d("error","error in key");
                break;
        }

    }

    @Override
    public void addCheckedItem(String key, String position) {
        switch (key)
        {
            case "price" :
                price.getCheckedPosition().add(position);
                break;
            case "brands" :
                brands.getCheckedPosition().add(position);
                break;
            case "engine" :
                engine.getCheckedPosition().add(position);
                break;
            case "style" :
                style.getCheckedPosition().add(position);
                break;
            case "ignition" :
                ignition.getCheckedPosition().add(position);
                break;
            case "sort" :
                sort.getCheckedPosition().add(position);
                break;
            default:
                Log.d("error","error in key");
                break;
        }
    }

    @Override
    public void removeCheckedItem(String key, String position) {
        switch (key)
        {
            case "price" :
                price.getCheckedPosition().remove(position);
                break;
            case "brands" :
                brands.getCheckedPosition().remove(position);
                break;
            case "engine" :
                engine.getCheckedPosition().remove(position);
                break;
            case "style" :
                style.getCheckedPosition().remove(position);
                break;
            case "ignition" :
                ignition.getCheckedPosition().remove(position);
                break;
            case "sort" :
                sort.getCheckedPosition().remove(position);
                break;
            default:
                Log.d("error","error in key");
                break;
        }
    }

    @Override
    public ArrayList<String> getCheckedItems(String key) {
        switch (key)
        {
            case "price" :
                return price.getCheckedPosition();
            case "brands" :
                return brands.getCheckedPosition();
            case "engine" :
                return engine.getCheckedPosition();
            case "style" :
                return style.getCheckedPosition();
            case "ignition" :
                return ignition.getCheckedPosition();
            case "sort" :
                return sort.getCheckedPosition();
            default:
                return null;
        }

    }


}
