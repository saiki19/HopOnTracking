package com.example.kishore.hopontracking_v3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class AccountDetails extends Activity {

    private static final String AccountInfo="AccountDetailsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        setDetails();

    }

    private void setDetails(){
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String CABID = settings.getString("cabid",null);
        String NUMPLATE = settings.getString("numplate","NONE");
        String DRIVER = settings.getString("drivername","NONE");
        String COMPANY = settings.getString("cabvendor","NONE");
        String CONTACT = settings.getString("phonenum","NONE");


        TextView cabid= (TextView)findViewById(R.id.acc_details_cabid);
        TextView numplate= (TextView)findViewById(R.id.acc_details_plateNo);
        TextView driver= (TextView)findViewById(R.id.acc_details_driver);
        TextView company= (TextView)findViewById(R.id.acc_details_vendor);
        TextView contact= (TextView)findViewById(R.id.acc_details_contact);

        cabid.setText(CABID);
        numplate.setText(NUMPLATE);
        driver.setText(DRIVER);
        company.setText(COMPANY);
        contact.setText(CONTACT);


    }

    public void editAccountDetails(View view){
        Intent intent = new Intent(this,editAccount.class);
        startActivity(intent);

    }



}
