package com.example.funquiz.Utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.funquiz.Controller.AppController;
import com.example.funquiz.Data.AsyncAnswerListResponse;
import com.example.funquiz.Model.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Repository {
    String apiUrl = "https://the-trivia-api.com/api/questions";
    List<Questions> Q_List = new LinkedList<>();
    JSONObject jsonObject;

    public List<Questions> getQ_List(final AsyncAnswerListResponse listResponse){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    for(int i=0;i<response.length();i++){
                        try {
                            jsonObject = response.getJSONObject(i);
                            Questions q = new Questions(jsonObject.getString("question")
                                    ,jsonObject.getString("correctAnswer"),
                                    jsonObject.getJSONArray("incorrectAnswers").getString(0)
                                    ,jsonObject.getJSONArray("incorrectAnswers").getString(1)
                                    ,jsonObject.getJSONArray("incorrectAnswers").getString(2));
                            Q_List.add(q);

                            Log.d("tag", "onSuccess : " +jsonObject.getString("question") );
                            Log.d("tag", "onSuccess : " +jsonObject.getString("correctAnswer") );
                            Log.d("tag", "Incorrect - 1 : "+jsonObject.getJSONArray("incorrectAnswers").getString(0));
                            Log.d("tag", "Incorrect - 2 : "+jsonObject.getJSONArray("incorrectAnswers").getString(1));
                            Log.d("tag", "Incorrect - 3 : "+jsonObject.getJSONArray("incorrectAnswers").getString(2));

                        } catch (JSONException e) {
                            Log.d("tag", "onResponse: "+e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    listResponse.processFinished(Q_List);
                }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "Make sure you have proper internet connection : "+error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return Q_List;
    }

}
