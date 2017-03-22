package hinder.hinder;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lstev030 on 3/15/2017.
 */

public class HinderRequestQueue {
    private static HinderRequestQueue mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    //Constructor
    private HinderRequestQueue(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized HinderRequestQueue getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new HinderRequestQueue(context);
        }
        return mInstance;
    }

    public void addToRequestQueue(Request request) {
        requestQueue.add(request);
    }
}