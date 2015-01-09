package com.example.kishore.hopontracking_v3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


public class splashscreen extends Activity {

    private static final String AccountInfo="AccountDetailsFile";
    private static int SPLASH_SCREEN_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //SharedPreferences settings = getSharedPreferences();
                //String CABID = settings.getString("cabid",null);

                if(settings.contains("cabid")){

                    Intent intent = new Intent(splashscreen.this,MainActivity.class);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(splashscreen.this, registercab.class);
                    startActivity(intent);


                }
            }
        },SPLASH_SCREEN_TIME);



    }


}
