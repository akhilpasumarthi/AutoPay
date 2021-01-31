package com.example.autopay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class Mapsview extends AppCompatActivity {


    Spinner spType;
    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat=0,currentLong=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsview);

        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemap);

        final String[] placeTypeList = {"gas_station","toll_booth","parking","movie_theater"};
        String[] placeNameList = {"Gas_Station","Toll_Booth","Parking","Movie_Theater"};

        spType.setAdapter(new ArrayAdapter<>(Mapsview.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Mapsview.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }else{

            ActivityCompat.requestPermissions(Mapsview.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }


        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=spType.getSelectedItemPosition();
                String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json"+ "?location="+currentLat+","+currentLong+"&radius=1000"+"&types="+placeTypeList[i]+"&sensor=true"+"&key="
                        +getResources().getString(R.string.google_map_key);
                Log.i("the url is",url);


                new PlaceTask().execute(url);

            }
        });


    }

    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){

                    currentLat=location.getLatitude();
                    currentLong=location.getLongitude();

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng everest = new LatLng(currentLat, currentLong);
                            map=googleMap;
                            //map.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(currentLat,currentLong)
                            //,10));
                            map.addMarker(new MarkerOptions().position(everest).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).visible(true));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(everest,10));

                        }
                    });
                }

            }
        });

    }
    public void onRequestPermissionResult(int requestCode, String[] permissions,int[] grantResults )
    {
        if(requestCode==44){
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                getCurrentLocation();
            }
        }
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... strings) {
            String data=null;
            try {
                data=downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url=new URL(string);
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream=connection.getInputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder=new StringBuilder();
        String line="";
        while((line=reader.readLine())!=null){

            builder.append(line);
        }
        String data=builder.toString();
        reader.close();
        return data;
    }

    private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String,String >>>{


        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            JsonParser jsonParser=new JsonParser();
            List<HashMap<String,String>> mapList=null;
            JSONObject object=null;

            try{
                object= new JSONObject(strings[0]);
                mapList=jsonParser.paresResult(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {

            map.clear();
            double num[]=new double[hashMaps.size()];
            double price[]={63,64,65,66,67,64,65,66,67,68};
            double kmcost[]={5,5.5,6.0};
            double lat[]=new double[hashMaps.size()];
            double lng[]=new double[hashMaps.size()];
            for(int i=0;i<hashMaps.size();i++){
                HashMap<String,String> hashMapList=hashMaps.get(i);
                lat[i]= Double.parseDouble(hashMapList.get("lat"));
                lng[i]= Double.parseDouble(hashMapList.get("lng"));
                String name=hashMapList.get("name");
                LatLng latLng=new LatLng(lat[i],lng[i]);
                LatLng latLng1=new LatLng(currentLat,currentLong);
                MarkerOptions options=new MarkerOptions();
                getCurrentLocation();
                num[i]=CalculationByDistance(latLng1, latLng, price[i], kmcost[i]);
                // Location.distanceBetween(currentLat,currentLong,lat, lng, num);
                // Log.i("the distance", String.valueOf(results[0]));
                options.position(latLng);
                options.title(name);
                options.snippet("Price "+price[i]);
                map.addMarker(options);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,7));
            }
            double min=num[0];
            int count=0;
            for(int j=0;j<num.length;j++){
                if(num[j]<min) {
                    min=num[j];
                    count=j;
                }
            }
            Log.i("minimum", String.valueOf(min));
            Log.i("minimum", String.valueOf(count));
            LatLng latLng3=new LatLng(lat[count],lng[count]);
            MarkerOptions options1=new MarkerOptions();
            options1.position(latLng3);
            options1.title("best one");
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            map.addMarker(options1);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng3,7));

        }
        public double CalculationByDistance(LatLng StartP, LatLng EndP,double p,double k) {
            int Radius = 6371;// radius of earth in Km
            double lat1 = StartP.latitude;
            double lat2 = EndP.latitude;
            double lon1 = StartP.longitude;
            double lon2 = EndP.longitude;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            double valueResult = Radius * c;
            double total=valueResult*k+p;
            double km = valueResult / 1;
            DecimalFormat newFormat = new DecimalFormat("####");
            int kmInDec = Integer.valueOf(newFormat.format(km));
            double meter = valueResult % 1000;
            int meterInDec = Integer.valueOf(newFormat.format(meter));
            Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec);

            return total;
        }
    }
}