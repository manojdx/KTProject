package com.miniproj.activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miniproj.R;
import com.miniproj.activity.Utils.Constants;
import com.miniproj.activity.realm.Latilng;
import com.miniproj.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<Latilng> latLngmodelArrayList = new ArrayList<Latilng>();
    private Realm realm;
    ImageView leftArrow,rightArrow;
    int count =1;
    int size =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);
        leftArrow = findViewById(R.id.left);
        rightArrow = findViewById(R.id.right);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        size = Constants.latilngsStatic.size();

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int result = count--;



                if(result>0){
                    updateMap(result);
                } else {
                    if(result==0){
                        Toast.makeText(getApplicationContext(),"Reached First",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // updateMap(count--);
                 int result = count++;
                if(result<size){
                    updateMap(result);
                } else {
                    if(result==size){
                        Toast.makeText(getApplicationContext(),"Reached End",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



       // realm = Realm.getDefaultInstance();
        latLngmodelArrayList.clear();
      /*  if (getIntent().hasExtra("bundle")) {
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null)
                latLngmodelArrayList = (ArrayList<Latilng>) bundle.getSerializable("latlng");
            else
                Log.e("null","null");
        }
        if (latLngmodelArrayList!=null) {
            Toast.makeText(getApplicationContext(),"Lat Lng List"+latLngmodelArrayList.toString(),Toast.LENGTH_SHORT).show();

        }*/


    }

    private void updateMap(int count) {
        if (count==0) {
            count =0;
        } else if(count>0){
            count = count-1;
        }
        Double lat = Double.valueOf(Constants.latilngsStatic.get(count).getLat());
        Double lng = Double.valueOf(Constants.latilngsStatic.get(count).getLng());
        LatLng sydney = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sector "+count));

       // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null);

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(sydney);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (count==1) {
            updateMap(count);
        }
    }
}