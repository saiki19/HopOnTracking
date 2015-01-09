package com.example.kishore.hopontracking_v3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class registercab extends Activity {

    public static final String AccountDetails = "AccountDetailsFile";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercab);
    }


    public void sendAccountDetails(View view) {

        new MyAsyncTask().execute();



    }


    private class MyAsyncTask extends android.os.AsyncTask <String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub
            postdata();
            Integer result= 1;
            return result;
        }


        @Override
        protected void onPostExecute(Integer result){

            Intent intent = new Intent(registercab.this,MainActivity.class);
            startActivity(intent);


        }


    }

    public Void postdata() {

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("https://www.hopon.co.in/corporate/php/cabregistration.php");

        try {
            // get data from the form

            EditText editText = (EditText) findViewById(R.id.cabid);
            EditText editText1 = (EditText) findViewById(R.id.NoPlate);
            EditText editText2 = (EditText) findViewById(R.id.drivername);
            EditText editText3 = (EditText) findViewById(R.id.company);
            EditText editText4 = (EditText) findViewById(R.id.phoneNo);
            Button RegisterButton = (Button) findViewById(R.id.Register);

            String cabid = editText.getText().toString();
            String numplate = editText1.getText().toString();
            String drivername = editText2.getText().toString();
            String cabvendor = editText3.getText().toString();
            String phonenum = editText4.getText().toString();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("cabid", cabid));
            nameValuePairs.add(new BasicNameValuePair("numplate", numplate));
            nameValuePairs.add(new BasicNameValuePair("drivername", drivername));
            nameValuePairs.add(new BasicNameValuePair("cabvendor", cabvendor));
            nameValuePairs.add(new BasicNameValuePair("phonenum", phonenum));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("cabid", cabid);
            editor.commit();
            editor.putString("numplate", numplate);
            editor.commit();
            editor.putString("drivername", drivername);
            editor.commit();
            editor.putString("cabvendor", cabvendor);
            editor.commit();
            editor.putString("phonenum", phonenum);
            editor.commit();






        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
