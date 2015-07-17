package com.karan.bikedekhoproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karan.bikedekhoproject.Adapter.bikeAdapter;
import com.karan.bikedekhoproject.Engine.Session;
import com.karan.bikedekhoproject.Entity.Bike;
import com.karan.bikedekhoproject.Entity.FilterElements;
import com.karan.bikedekhoproject.Entity.FilterParams;
import com.karan.bikedekhoproject.Utils.MyEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;


public class ViewActivity extends ActionBarActivity {

    private StringRequest req;
    private JsonObjectRequest req1;
    private String newBikeFilterUrl = "http://api.testing.bikedekho.com/v1/test/allFilterTag?_format=json";
    public static FilterParams filterParams;
    private ArrayList<Bike> bikes;
    private Bike bike;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private bikeAdapter bkAdapter;
    private ImageLoader mImageLoader;
    private String sortingMethod="price.asc";
    private ImageButton imageButton;
    private FilterElements filterElements;
    private JSONObject filter;
    private int totalCount;
    private int count = 1;
    boolean flag = true;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    int visibleItemCount, pastVisiblesItems, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(ViewActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        rv = (RecyclerView) findViewById(R.id.rv);
        bikes = new ArrayList<Bike>();
        llm = new LinearLayoutManager(getApplicationContext());

        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();


                if(flag&&visibleItemCount == totalItemCount - pastVisiblesItems&&totalCount>count*20)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    flag = false;
                    Toast.makeText(getApplicationContext(),"Loading more bikes",Toast.LENGTH_LONG).show();
                    count++;
                    try {
                        filterLoad();
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        imageButton = (ImageButton) findViewById(R.id.filter);
        imageButton.setVisibility(View.GONE); // Will make visible when data is fetched

        /*Image loader is Volley's image Que Deque and Cache object */

        ImageLoader.ImageCache imageCache = new LRUBitmapCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);


        req = Session.getmInstance().generateFilterRESPONSE(newBikeFilterUrl);
        addReqToQueue(req, "filter");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterElements != null) {
                    Intent i = new Intent(getApplicationContext(), FilterActivity.class);
                    i.putExtra("filterList", filterElements);
                    startActivityForResult(i, 12);
                }
            }
        });

        try {
            firstLoad();
        } catch (JSONException e) {
            Log.d("JSONException", e.toString());
        }

    }

    public void firstLoad() throws JSONException {
        /* Initialization based on example given in doc*/
        filterParams = new FilterParams();
        /*Filter object to create initial filter json object*/
        filter = new JSONObject();
        filter.put("brand", filterParams.getBrand());
        filter.put("price", filterParams.getPrice());
        filter.put("fuelType", filterParams.getFuelType());
        filter.put("city", filterParams.getCity());
        filter.put("style", filterParams.getStyle());
        filter.put("start_option", filterParams.getStart_option());
        filter.put("cc", filterParams.getCc());

        Log.d("Request/Bike/FirstLoad",filter.toString());

        req1 = Session.getmInstance().generateFilterRequest("http://api.testing.bikedekho.com/v1/test/result?_format=json", filter, "1", "20", sortingMethod);
        addReqToQueue(req1, "bikes");
    }

    public void filterLoad() throws JSONException {
        JsonObjectRequest req;
        if(count!=1)
            req = Session.getmInstance().generateFilterRequest("http://api.testing.bikedekho.com/v1/test/result?_format=json", filter, String.valueOf(count*10), String.valueOf(count*20), sortingMethod);
       else
            req = Session.getmInstance().generateFilterRequest("http://api.testing.bikedekho.com/v1/test/result?_format=json", filter, String.valueOf(1), String.valueOf(20), sortingMethod);

        addReqToQueue(req, "bikesFilter");
    }

    /*String Request*/
    public void addReqToQueue(StringRequest temp, String tag) {
        Session.getmInstance().addToRequestQueue(temp, tag);
    }

    /*Json Request*/
    public void addReqToQueue(JsonObjectRequest temp, String tag) {
        Session.getmInstance().addToRequestQueue(temp, tag);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void onEventMainThread(MyEvents event) {
        switch (event.getType()) {
            case MyEvents.BIKE_RESPONSE: {
               if( progressDialog.isShowing())
                   progressDialog.hide();
                if (event.isStatus()) {
                    Log.d("Response/Bikes", event.getValue().toString());
                    try {
                        JSONObject response = new JSONObject(event.getValue().toString());
                        JSONObject data = response.getJSONObject("data");
                        JSONArray newBikes = data.getJSONArray("newBikes");

                        totalCount = Integer.valueOf(data.getString("count"));

                        for (int i = 0; i < newBikes.length(); i++) {
                            bike = new Bike();
                            bike.setCc(newBikes.getJSONObject(i).getString("engineCapacity"));
                            bike.setAmount(newBikes.getJSONObject(i).getString("price"));
                            bike.setKmpl(newBikes.getJSONObject(i).getString("mileage"));
                            bike.setName(newBikes.getJSONObject(i).getString("display_name"));
                            bike.setImageURL(newBikes.getJSONObject(i).getString("image"));
                            bikes.add(bike);
                            bike = null;
                        }
                        rv.setLayoutManager(llm);

                        if(count == 1) {
                        /*bikes --> List of Bike objects ||||  mImageLoader --> Image Q dQ and Cache*/
                            bkAdapter = new bikeAdapter(bikes, this, mImageLoader);
                            rv.setAdapter(bkAdapter);
                        }else if(totalCount>count*20){
                            bkAdapter.notifyDataSetChanged();
                            flag=true;
                            progressBar.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server down, please retry after sometime", Toast.LENGTH_LONG).show();
                }
            }
            break;


            case MyEvents.FILTER_RESPONSE: {
                if (event.isStatus()) {
                    Log.d("Response/Filters", event.getValue().toString());
                    try {
                        filterElements = new FilterElements();
                        JSONObject jsonObject = new JSONObject(event.getValue().toString());
                        jsonObject = jsonObject.getJSONObject("data");

                        filterElements.setBrand(jsonObject.getJSONArray("brand").toString());
                        filterElements.setPrice(jsonObject.getJSONArray("price_range").toString());
                        filterElements.setEngine(jsonObject.getJSONArray("cc").toString());
                        filterElements.setStyle(jsonObject.getJSONArray("style").toString());
                        filterElements.setIgnition(jsonObject.getJSONArray("start_option").toString());
                        filterElements.setSort(jsonObject.getJSONArray("sort_tag").toString());
                        imageButton.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server down, please retry after sometime", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    try {

                        filter = null;
                        filter = new JSONObject(b.getSerializable("filter").toString());
                        if(b.getSerializable("sort")!=null) sortingMethod = b.getSerializable("sort").toString();


                        Log.d("Filter :", filter.toString());
                        progressBar.setVisibility(View.GONE);
                        bikes = new ArrayList<Bike>();
                        count=1;
                        flag = true;
                        progressDialog.show();
                        filterLoad();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == 0) {

            }
        }
    }
}