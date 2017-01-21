package com.xu.bombventure;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.support.v4.app.Fragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

//Basically two programs here, one for maps, one for finding location
//http://stackoverflow.com/questions/31573094/how-to-get-current-location-of-device-on-google-maps-api-v2
//http://www.androidtrainee.com/adding-and-removing-multiple-proximity-alerts-in-google-maps-android-api-v2/

//04122016 ZOOM AND INITIAL LOCATION FINALLY WORKS!!!
//Now, fix the proximity alert sharedpreferences-drawmarker system. Causing nullpointer exception each time
//http://wptrafficanalyzer.in/blog/adding-and-removing-proximity-alert-in-google-map-android-api-v2-using-locationmanager/
// I think I may have clicked map, causing proxalert nullpointer exception?

//TODO add in the popUp class when clicked? then on long click launch the actual page

public class MainActivity extends FragmentActivity implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent proximityIntent;
    SharedPreferences sharedPreferences;
    SharedPreferences userPreferences;
    int locationCount=0;
    private static final String PROX_ALERT_INTENT ="com.xu.bombventure";
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    //Code for GoogleApiClient, and LocationListener
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat, lon;
    double userlat, userlon;
    Location userloc;
    Marker mCurrLocationMarker;
    TextView coinView;
    int uniqueId;
    RoomDB rDB;
    HashMap<Integer,Room> roomDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if Google Play is available
        boolean status = isGooglePlayServicesAvailable(this);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();

        //Gaining access to Singleton DB with dummy data
        rDB=RoomDB.get(this);
        roomDB= rDB.getRooms();

        checkLocationPermission();
        if (status) {
            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            // Getting GoogleMap object from Map Fragment
            //googleMap = fm.getMapAsync(this); - YOU CANNOT ASSIGN A MAPFRAGMENT TO A GOOGLEMAP
             fm.getMapAsync(this);

            userPreferences = getSharedPreferences("user", 0);
            int current=userPreferences.getInt("points",10);



            //mapAsync calls onMapReady(), along with its subordinate build googleServices
            //ALL GOOGLEMAP RELATED CODE HAS TO BE IN ONMAPREADY

                    //Set current view to current location



            //Get locationManager object from System Service
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //fetch sharedpreferences object
            sharedPreferences = getSharedPreferences("location", 0);
            //In future versions, should use DB server

            //getting number of proximity alert locations
            locationCount = sharedPreferences.getInt("locationCount", 0);


            //Register the BroadcastReceiver DefuseReceiver for any PROX Alerts
            //THIS IS DUPLICATED IN ONMAPREADY, HENCE TEMPORARILY REMOVED
            //IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
            //registerReceiver(new DefuseReceiver(), filter);


           /* //Iterating through locationCounts
            if (locationCount != 0) {
                String lat = "";
                String lng = "";

                //Iterating through locations stored
                //Draw marker and circles for each location stored
                for (int i = 0; i < locationCount; i++) {
                    //getting latitutude of i-th location
                    lat = sharedPreferences.getString("lat" + i, "0");
                    //getting longitude of the i-th location
                    lng = sharedPreferences.getString("lng" + i, "0");

                    //DRAW MARKER ON MAP, if latitude has nonzero value
                    if (lat.equals("0")){
                        //
                        drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                        //DRAW CIRCLE ON MAP
                        drawCircle(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                    }


                }
 */
                //Move CamerPosition to last proxed position, not necessary?
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                //set Correct zoom Level, 20- buildings


            }

        }


    //Required code for LocationListener, onConnected to Google Play Services, we will find
    @Override
    public void onConnected(Bundle bundle) {

        checkLocationPermission();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
    }
        //if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

        //    buildGoogleApiClient();
          //  mGoogleApiClient.connect();

       // }


        //NOT NECESSARY/CONFLICTING CODE?  connection, allow map to display users latest location

       // mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //if (mLastLocation != null) {
          //  lat = mLastLocation.getLatitude();
          //  lon = mLastLocation.getLongitude();
          //  LatLng loc = new LatLng(lat, lon);
          //  googleMap.addMarker(new MarkerOptions().position(loc).title("Current Loction"));
          //  googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        //}

    //}
    @Override
    public void onConnectionSuspended(int i){

    }

    //locationlistener code, update camera movement on Location
    @Override
    public void onLocationChanged(Location location){
        mLastLocation=location;
        lat  = mLastLocation.getLatitude();
        lon = mLastLocation.getLongitude();


        //check if marker exists in old place, if so then remove it
        if (mCurrLocationMarker!= null){
            mCurrLocationMarker.remove();
        }
        LatLng loc = new LatLng(lat,lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loc);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = googleMap.addMarker(markerOptions);

        // without zoom : googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,17));


        //stop location updates
        //if(googleMap !=null){
           // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
       // }
    }

    @Override
     public void onConnectionFailed(ConnectionResult connectionResult){
        buildGoogleApiClient();
     }

    synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    };
    //Connect and disconnect to ApiClient during start/destroy
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

            buildGoogleApiClient();


        }

        mGoogleApiClient.connect();



    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }




    //Code required for getMapAsync, part of onMapReadyCallback
    //GetMapAsync needs hence this callback method, where you can immediately set stuff
    @Override
    public void onMapReady(GoogleMap map ) {


        this.googleMap = map;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        //all of this code should be moved to setUpMap();
        map.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Custom Map UI set up
        //disable zoom Controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        CameraUpdateFactory.zoomTo(17.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location granted

                googleMap.setMyLocationEnabled(true);
            } else {
                //Request location permission
                checkLocationPermission();
            }
        } else {

            googleMap.setMyLocationEnabled(true);
        }



        if (locationCount != 0) {
            String latitude = "";
            String longitude = "";

            //Iterating through locations stored
            //Draw marker and circles for each location stored
            for (int i = 0; i < locationCount; i++) {
                //getting latitutude of i-th location
                latitude = sharedPreferences.getString("lat" + i, "0");
                //getting longitude of the i-th location
                longitude = sharedPreferences.getString("lng" + i, "0");

                //DRAW MARKER ON MAP, if latitude has nonzero value
                if (latitude.equals("0")) {
                    //
                    drawMarker(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));

                    //DRAW CIRCLE ON MAP
                    drawCircle(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                }


            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String title = marker.getTitle();
                    return false;
                }
            });
            //Marker Adding
            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                @Override
                public void onMarkerDragStart(Marker marker) {
                    //Launch into detailView, simulating onLongClick
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    // TODO Auto-generated method stub

                }
            });


            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {

                    checkLocationPermission();
                    //increment LocationCount
                    locationCount++;




                    //TODO Should add a UniqueID based on which question we are seeing, defined as latlngpair?
                    uniqueId = (int) lat;
                    //now set the Intent Filter action to be unique, we will register the receiver with it
                    String intentAction = "PROXIMITY_ALERT" + uniqueId;
                    Intent intent = new Intent(intentAction);
                    //Start placing things within the intent
                    proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), uniqueId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    intent.putExtra(DefuseReceiver.location_no, locationCount);
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


                        drawMarker(point);
                        drawCircle(point);





                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("lat" + Integer.toString((locationCount - 1)), Double.toString(userlat));
                    editor.putString("lng" + Integer.toString((locationCount - 1)), Double.toString(userlon));
                    editor.putInt("locationCount", locationCount);

                    editor.commit();


                }
            });

            //Long click currntly removes the last placed pendingIntent
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {


                    checkLocationPermission();
                    //remove proximityAlert, must use same pendingIntent as addProximityAlert
                    locationManager.removeProximityAlert(proximityIntent);

                    googleMap.clear();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(getBaseContext(), "Prox alert removed", Toast.LENGTH_SHORT);
                }
            });


        }
    }



    private void drawCircle(LatLng point) {
        //CircleOptions needed
        CircleOptions circleOptions = new CircleOptions();
        //find center of circle
        circleOptions.center(point);
        //radius of the circle
        circleOptions.radius(10);
        //Border color of circle
        circleOptions.strokeColor(Color.BLACK);
        //fillcolor of circle
        circleOptions.fillColor(0x30ff0000);
        //Border width of circle
        circleOptions.strokeWidth(2);

        googleMap.addCircle(circleOptions);

        //add circle to googleMap
    }

    private void drawMarker(LatLng point) {
        //create MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("Room");

        markerOptions.snippet(point.latitude+","+point.longitude);
        //TODO add in the question data to snippet
        markerOptions.snippet(Double.toString(point.latitude) + ',' + Double.toString(point.longitude));
        //adding marker on googlemap, set draggable to simulate onlongclick
        googleMap.addMarker(markerOptions).setDraggable(true);;



    }







    public boolean isGooglePlayServicesAvailable(Activity activity){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status= api.isGooglePlayServicesAvailable(this);
        //if we have a problem, return false
        if(status != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(status)) {
                api.getErrorDialog(activity, status, 2404).show();
            }
            return false;

        }
        return true;
    }

    //required to check for when the user doesnt allow permission
    public static final int MY_PERMISSIONS_REQUEST_LOCATION=99;
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            //No permission allowed, force user to give one
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);


        }
        return true;

    }
    //callback from RequestPermissions() method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int []grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                //if request is cancelled result arrays are empty
                if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    //permission granted, so do everything related to locations
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if (mGoogleApiClient == null){
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                }else{

                    //permission denied
                    Toast.makeText(this,"permission denied, app functionality disabled", Toast.LENGTH_LONG).show();
                    }
                return;
            }
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        //Required CONNECT CALL TO ACTUALLY START FUSED LOCATION API

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){


            mGoogleApiClient.connect();

        }

        if (googleMap == null) {
            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

            fm.getMapAsync(this);
        }


        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new DefuseReceiver(), filter);
    }
    @Override
    public void onPause(){
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        try{
            unregisterReceiver(new DefuseReceiver());
        }catch (final Exception exception){
            //was already unregistered, so do nothing
        }

    }

}
