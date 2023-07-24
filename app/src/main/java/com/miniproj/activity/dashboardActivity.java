package com.miniproj.activity;

import static android.os.Build.VERSION.SDK_INT;
import static com.miniproj.activity.Utils.Constants.BroadcastFirstConst;
import static com.miniproj.activity.Utils.Constants.StartScrVisibility;
import static com.miniproj.activity.Utils.Constants.broadcastResStatus;
import static com.miniproj.activity.Utils.Constants.latilngsStatic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.miniproj.R;
import com.miniproj.activity.Utils.Constants;
import com.miniproj.activity.Utils.PreferenceManager;
import com.miniproj.activity.Utils.Preference_Details;
import com.miniproj.activity.adapter.LocationAdapter;
import com.miniproj.activity.interfaces.Preference_Keys;
import com.miniproj.activity.realm.Latilng;
import com.miniproj.activity.realm.LatilngClone;
import com.miniproj.activity.realm.Register;
import com.miniproj.activity.service.ForegroundService;

import java.util.ArrayList;
import java.util.List;

import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class dashboardActivity extends AppCompatActivity implements LocationListener{

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    androidx.appcompat.widget.AppCompatImageView signout;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Double latitude = 0.0, longitude = 0.0;
    LocationManager locationManager;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private boolean mBound = false;
    private GnssStatus.Callback mGnssStatusCallback ;
    TextView mapView;
    Context mContext;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 500 * 60 ;
    public static final String RECEIVE_latLng = "com.miniproj.RECEIVE_latLng";
    private String TAG = dashboardActivity.class.getSimpleName();
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;
    Double lat = 0.0;
    Double longi = 0.0;
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    private ForegroundService mService = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    TextView MapView;
    RecyclerView recyclerview;
    ArrayList<Latilng> lngmodelArrayList = new ArrayList<>();
    LocationAdapter locationAdapter;
    LinearLayoutManager RecyclerViewLayoutManager;
    private Realm realm;
    RealmList<LatilngClone> latilngClones = new RealmList<>();
    TextView dash_title;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ForegroundService.LocalBinder binder = (ForegroundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
             mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                .permitDiskWrites()
                .build());
        StrictMode.setThreadPolicy(old);
        setContentView(R.layout.activity_dashboard);
        dash_title = findViewById(R.id.dash_title);
        dash_title.setText("Hi,"+Preference_Details.getDisplayName());
       // realm = Realm.getDefaultInstance();
        recyclerview = findViewById(R.id.recyclerview);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerViewLayoutManager.setAutoMeasureEnabled(false);
        recyclerview.setLayoutManager(RecyclerViewLayoutManager);

        MapView = findViewById(R.id.mapView);
        MapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager.removeUpdates(locationListenerGPS);
              //  updateData();
                Constants.latilngsStatic.clear();
                latilngsStatic.addAll(lngmodelArrayList);
                Intent mapViewIntent = new Intent(getApplicationContext(),MapsActivity.class);
               /* Bundle bundle = new Bundle();
                bundle.putSerializable("latlng",lngmodelArrayList);
                mapViewIntent.putExtra("bundle",bundle);*/
                int listCount = Constants.latilngsStatic.size();
                if (listCount>0) {
                    startActivity(mapViewIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"no Location Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myReceiver = new MyReceiver();
        if (BroadcastFirstConst == false) {
            BroadcastFirstConst = true;
            LocalBroadcastManager.getInstance(dashboardActivity.this).registerReceiver(myReceiver, new IntentFilter(ForegroundService.ACTION_BROADCAST));
        } else {
            broadcastResStatus = true;
        }
        signout = findViewById(R.id.signout);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        mContext = this;
        if (checkLocationPermission()) {
           // init_location_start();
            locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                    500,
                    500, locationListenerGPS);
        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            // name.setText(personName);
            // email.setText(personEmail);
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        getBackgroundPermission();
        if (SDK_INT >= Build.VERSION_CODES.N) { //14/12 Anitha
            Log.d(TAG, "check crash get gps on create"); //15/12
            getGps();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    private void getGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

    }








    private void getBackgroundPermission() {
        if (!(SDK_INT >= Build.VERSION_CODES.Q)) {
            if ((ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)==false){
                //ShowAlertError(RoadSettingsPageActivity.this,"Please click\n\"Allow all the time\"","OK",RoadSettingsPageActivity.this);
            }
        }else {
            if ((ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)==false){
                Toast.makeText(this,"Please click Allow all the time",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "check myreceiver crash init: ");
            Location location = intent.getParcelableExtra(ForegroundService.EXTRA_LOCATION);
            Log.d(TAG, "location in myreceiver: " + location);

            if (broadcastResStatus == true) {
                broadcastResStatus = false;
                LocalBroadcastManager.getInstance(dashboardActivity.this).unregisterReceiver(myReceiver);
                //LocalBroadcastManager.getInstance(RoadStartPageActivity.this).registerReceiver(myReceiver, new IntentFilter(GPSTracker.ACTION_BROADCAST));
            }
            if (location != null) {

//                    Log.d("LOOCATIONS", String.valueOf(Util_functions.getLocationText(location)));

                    lat = location.getLatitude();
                    longi = location.getLongitude();

                    Log.d("lat & lng", lat + " " + longi);

                    Log.d(TAG, "check myreceiver crash: ");     //Anitha 15/12


                    Log.d(TAG, "check myreceiver crash 1: ");     //Anitha 15/12

//                    if (Preference_Details.getBgStart() == true && Constants.BgStart == true && isChecked == true && Constants.GPS_deviceID != ""){
//                        autoStart=false;
//                    }

//                    getUpdateHandler(); //16/12 background ui not working
            }


        }
    }

    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
             latitude=location.getLatitude();
             longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
         //   Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            Latilng latLngmodel = new Latilng();
            latLngmodel.setLat(String.valueOf(latitude));
            latLngmodel.setLng(String.valueOf(longitude));
            lngmodelArrayList.add(latLngmodel);
            if (lngmodelArrayList.size()>0) {
                locationAdapter = new LocationAdapter(lngmodelArrayList,getApplicationContext());
                recyclerview.setAdapter(locationAdapter);
            } else {
                locationAdapter.updateList(lngmodelArrayList);
                locationAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                lngmodelArrayList.clear();
                latilngsStatic.clear();
                gsc.signOut();
                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,false);
                locationManager.removeUpdates(locationListenerGPS);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(dashboardActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (StartScrVisibility == true) {
                            mService.requestLocationUpdates(dashboardActivity.this);
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(dashboardActivity.this).registerReceiver(myReceiver, new IntentFilter(ForegroundService.ACTION_BROADCAST));
        StartScrVisibility = true;
        if (latilngsStatic.size()>0) {
         /*   locationAdapter = new LocationAdapter(latilngsStatic, getApplicationContext());
            recyclerview.setAdapter(locationAdapter);*/
            locationAdapter.updateList(lngmodelArrayList);
            locationAdapter.notifyDataSetChanged();
        }
       /* } else {
            locationAdapter.updateList(lngmodelArrayList);
            locationAdapter.notifyDataSetChanged();
        }*/

    }


    @Override
    protected void onPause() {
        super.onPause();
        // locationManager.removeUpdates(locationListenerGPS);
         startService(new Intent(getApplicationContext(), ForegroundService.class));
        StartScrVisibility = false;

    }


    private void init_location_start() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                //  mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                lat = mCurrentLocation.getLatitude();
                longi = mCurrentLocation.getLongitude();
                Log.d("lat & lng", lat + " " + longi);
                String msg="New Latitude: "+lat + "New Longitude: "+longi;
             //   Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();


            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ForegroundService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
      /*  android.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);*/
        super.onStop();
        // mTimerHandler.removeCallbacksAndMessages(mTimerRunnable);
        //   super.onStop();



        //Toast.makeText(RoadStartPageActivity.this, "stopp", Toast.LENGTH_SHORT).show();
        //   StartScrVisibility = false;
    }

}