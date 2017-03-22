package hinder.hinder;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lstev030 on 3/15/2017.
 */

public class DeezNutz {
    private static DeezNutz mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    //Constructor
    private DeezNutz(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized DeezNutz getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new DeezNutz(context);
        }
        return mInstance;
    }

    public void addToRequestQueue(Request request) {
        requestQueue.add(request);
    }
}