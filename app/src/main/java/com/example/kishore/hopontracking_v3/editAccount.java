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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class editAccount extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        setDetails();


    }

    public void updatedetails(View view){

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

            Intent intent = new Intent(editAccount.this,AccountDetails.class);
            startActivity(intent);


        }


    }

    public Void postdata() {

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("https://www.hopon.co.in/corporate/php/updatecabregistrationdetails.php");

        try {
            // get data from the form

            TextView editText = (TextView) findViewById(R.id.acc_details_cabid);
            TextView editText1 = (TextView) findViewById(R.id.acc_details_plateNo);
            EditText editText2 = (EditText) findViewById(R.id.edit_driver);
            TextView editText3 = (TextView) findViewById(R.id.acc_details_vendor);
            EditText editText4 = (EditText) findViewById(R.id.edit_contact);
            Button UpdateButton = (Button) findViewById(R.id.Update);

            String cabid = ((TextView)editText).getText().toString();
            String numplate = ((TextView)editText1).getText().toString();
            String drivername = editText2.getText().toString();
            String cabvendor = ((TextView)editText3).getText().toString();
            String phonenum = editText4.getText().toString();

            //assign it in a name value pair that is used to encode in the url

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("cabid", cabid));
            nameValuePairs.add(new BasicNameValuePair("numplate", numplate));
            nameValuePairs.add(new BasicNameValuePair("drivername", drivername));
            nameValuePairs.add(new BasicNameValuePair("cabvendor", cabvendor));
            nameValuePairs.add(new BasicNameValuePair("phonenum", phonenum));

            //encode it in the url
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // make the POST call
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

    private void setDetails(){
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String CABID = settings.getString("cabid",null);
        String NUMPLATE = settings.getString("numplate","NONE");
        String DRIVER = settings.getString("drivername","NONE");
        String COMPANY = settings.getString("cabvendor","NONE");
        String CONTACT = settings.getString("phonenum","NONE");


        TextView cabid= (TextView)findViewById(R.id.acc_details_cabid);
        TextView numplate= (TextView)findViewById(R.id.acc_details_plateNo);
        TextView company= (TextView)findViewById(R.id.acc_details_vendor);
        EditText driver= (EditText)findViewById(R.id.edit_driver);
        EditText contact=(EditText)findViewById(R.id.edit_contact);


        cabid.setText(CABID);
        numplate.setText(NUMPLATE);
        driver.setText(DRIVER);
        company.setText(COMPANY);
        contact.setText(CONTACT);


    }





}
