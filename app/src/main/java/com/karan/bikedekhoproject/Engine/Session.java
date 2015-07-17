package com.karan.bikedekhoproject.Engine;

import android.app.Application;
import android.app.usage.UsageEvents;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karan.bikedekhoproject.Utils.MyEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by karanahuja on 13/07/15.
 */
public class Session extends Application {

    public static final String TAG = Session.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private static Session mInstance;

    private EventBus eventBus = EventBus.getDefault();

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Session getmInstance()
    {
        return mInstance;
    }

    public RequestQueue getmRequestQueue(){
        if(mRequestQueue == null) {
             mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader(){
        return mImageLoader;
    }

    public <T> void  addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(req);
    }


    public <T> void  addToRequestQueue(Request<T> req){
        getmRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if(mRequestQueue!=null){
            mRequestQueue.cancelAll(tag);
        }

    }

    public StringRequest generateFilterRESPONSE(String url){
        StringRequest req  = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        eventBus.post(new MyEvents(MyEvents.FILTER_RESPONSE,true,response));
                        Log.d("Success",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                eventBus.post(new MyEvents(MyEvents.FILTER_RESPONSE,false,error));
                Log.d("error",error.toString());
            }
        }
        ){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<String,String>();
                headers.put("X-Public","testing");
                headers.put("X-Hash","testing");
                Log.d("Header for Filter", headers.toString());
                return headers;
            }

        };
        return req;
    }


    public JsonObjectRequest generateFilterRequest(String url, final JSONObject filter, final String start, final String end, final String sort_type) throws JSONException{

        JSONObject params = new JSONObject();
        params.put("filter",filter);
        params.put("start",start);
        params.put("end",end);
        params.put("sort_type",sort_type);

        Log.d("Params",params.toString());

        JsonObjectRequest req  = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        eventBus.post(new MyEvents(MyEvents.BIKE_RESPONSE,true,response));
                        Log.d("Success",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                eventBus.post(new MyEvents(MyEvents.BIKE_RESPONSE,false,error));
                Log.d("error",error.toString());
            }
        }
        ){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<String,String>();
                headers.put("X-Public","testing");
                headers.put("X-Hash", "testing");
               // headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        return req;
    }


}
