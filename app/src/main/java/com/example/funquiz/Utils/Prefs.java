package com.example.funquiz.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String HIGHEST_SCORE = "highest_score";

    SharedPreferences sharedPreferences;
   public  Prefs(Activity context){
        this.sharedPreferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void setHighestScore(int score_num){
        int current_score = score_num;
        int top_score = sharedPreferences.getInt(HIGHEST_SCORE,0);
        if(current_score>top_score){
            sharedPreferences.edit().putInt(HIGHEST_SCORE,current_score)
                    .apply();
        }
    }

    public int getHighestScore(){
        return sharedPreferences.getInt(HIGHEST_SCORE,0);
    }
}
