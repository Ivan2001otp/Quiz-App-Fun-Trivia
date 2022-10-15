package com.example.funquiz.Controller;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class AppController extends Application {
    public static AppController instance;
    public RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized AppController getInstance(){
        return instance;
    }

   public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
   }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());//mainactivity.this
        }
        return requestQueue;
    }



}
