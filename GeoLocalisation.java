package com.example.lyes.parking;


        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.widget.Toast;

        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;

public class GeoLocalisation extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    final static private int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_localisation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation=location;
                    Toast.makeText(GeoLocalisation.this,currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(GeoLocalisation.this);
                }

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


            LatLng latLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I'm here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
            googleMap.addMarker(markerOptions);

        LatLng latLng2=new LatLng(30,3);
        MarkerOptions markerOptions_2 = new MarkerOptions().position(latLng2).title("first parking");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2,5));
        googleMap.addMarker(markerOptions_2);


        LatLng latLng3=new LatLng(32,4);
        MarkerOptions markerOptions_3 = new MarkerOptions().position(latLng3).title("Second_park");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng3));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng3,5));
        googleMap.addMarker(markerOptions_3);



        LatLng latLng4=new LatLng(29,3);
        MarkerOptions markerOptions_4 = new MarkerOptions().position(latLng4).title("third_park");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng4));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng4,5));
        googleMap.addMarker(markerOptions_4);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    fetchLastLocation();
                break;
        }

    }
}
