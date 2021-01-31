package com.example.autopay;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap,mMap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap1 = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng everest = new LatLng(17.506607, 78.818447);
        LatLng one = new LatLng(17.490901, 78.675563);
        LatLng two = new LatLng(17.527925, 78.937617);
        LatLng three = new LatLng(17.627390, 78.852692);
        LatLng four = new LatLng(17.408341, 78.968352);
        LatLng five = new LatLng(17.423004, 78.855119);
        LatLng six = new LatLng(17.727829, 78.942452);
        mMap.addMarker(new MarkerOptions().position(everest).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdot)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(everest,10));
       // mMap1.moveCamera(CameraUpdateFactory.newLatLng(everest));
        //mMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(everest,30));
        mMap.addMarker(new MarkerOptions().position(one).title("AutoPay Gas station").snippet("Cost: 74 per litre").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(one,10));
        mMap.addMarker(new MarkerOptions().position(two).title("AutoPay Gas station").snippet("Cost: 74 per litre").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(two,10));
        mMap.addMarker(new MarkerOptions().position(three).title("AutoPay Gas station").snippet("Cost: 74 per litre").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(three,10));
        mMap.addMarker(new MarkerOptions().position(four).title("AutoPay parking").snippet("Cost: 150 per hour").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(four,10));
        mMap.addMarker(new MarkerOptions().position(five).title("AutoPay parking").snippet("Cost: 130 per hour").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(five,10));
        mMap.addMarker(new MarkerOptions().position(six).title("AutoPay parking").snippet("Cost: 115 per hour").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(six,10));
    }
}