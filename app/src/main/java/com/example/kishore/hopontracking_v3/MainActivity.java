package com.example.kishore.hopontracking_v3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    protected TextView mLatitudeText;
    protected TextView mLongitudeText;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //build google Api client
        buildGoogleApiClient();


//        mLatitudeText= (TextView) findViewById(R.id.latitude_text);
//        mLongitudeText= (TextView) findViewById(R.id.longitude_text);

        //connect to google api client

        mGoogleApiClient.connect();





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent= new Intent(this,AccountDetails.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

protected void createLocationRequest(){

    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(5000);
    mLocationRequest.setFastestInterval(2000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


}

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .addApi(LocationServices.API)
               .build();

        // set preferences for location request
        createLocationRequest();

    }


    @Override
    protected void onStart(){
        super.onStart();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();




    }

    @Override
    public void onConnected(Bundle connectionHint) {

        mMap.setMyLocationEnabled(true);
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

        LatLng latLng = getLatLng(mCurrentLocation);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String markerTitle = settings.getString("drivername","Current Location");

        MarkerOptions mOptions = new MarkerOptions()
                .position(latLng)
                .title(markerTitle);



        marker =  mMap.addMarker(mOptions);

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    protected void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    public void onLocationChanged(Location location){
        mCurrentLocation = location;
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
        LatLng latLng= getLatLng(mCurrentLocation);
        marker.setPosition(latLng);

        //mLatitudeText.setText(mLocationLatitude.toString());
        //mLongitudeText.setText(mLocationLongitude.toString());

        //send location to hopon server
        new sendLocationAsync().execute();


    }

    private class sendLocationAsync extends AsyncTask<String, Integer, Integer>{

        protected Integer doInBackground(String...Params){

            Integer dummy_result=1;
            postLocationData();
            return dummy_result;

        }

        protected void onPostExecute(Integer dummy_result){
            Toast.makeText(MainActivity.this, getResources().getString(R.string.httpsent_success),
                    Toast.LENGTH_SHORT).show();

        }

    }

    public void postLocationData(){

        HttpClient client = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("https://www.hopon.co.in/corporate/php/updateLocation.php");

        try {
            // get current location and cab id
            Double locationLat= mCurrentLocation.getLatitude();
            Double locationLon= mCurrentLocation.getLongitude();
            String latitude = locationLat.toString();
            String longitude = locationLon.toString();

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String cabID= settings.getString("cabid",null);

            // assign the information in the name value pair
            List <NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("cabid",cabID));
            nameValuePairs.add(new BasicNameValuePair("lat",latitude));
            nameValuePairs.add(new BasicNameValuePair("lon",longitude));


            //encode it in the url
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // make the POST call
            HttpResponse response = client.execute(httppost);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                LatLng mLatLng= new LatLng(0,0);
//                mMap.setMyLocationEnabled(true);
//                setUpMap(mLatLng);
//            }
        }
    }

    public LatLng getLatLng(Location location){
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        LatLng latLng = new LatLng(lat,lng);
        return latLng;
    }


}
