package com.example.thedrop;

import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int DEVICE_LOCATION_PERMISSION = 0;


    TextView scan_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        scan_result = (TextView)findViewById(R.id.scan_result);
    }

    public void scanCode(View v) {
        Intent intent = new Intent(this,BarcodeDetect.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==0) {
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                     Barcode barcode = data.getParcelableExtra("barcode");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(barcode.displayValue));
                        startActivity(browserIntent);
                } else {
                    //scan_result.setText("No barcode found");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        LatLng loc_hfh = new LatLng(34.413745, -119.841669);
        mMap.addMarker(new MarkerOptions().position(loc_hfh).title("HFH Drop"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_hfh));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    DEVICE_LOCATION_PERMISSION);
        }

    }
}
