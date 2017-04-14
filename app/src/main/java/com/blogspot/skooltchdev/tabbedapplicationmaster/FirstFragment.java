package com.blogspot.skooltchdev.tabbedapplicationmaster;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;


public class FirstFragment extends Fragment {
    private static final int MY_REQUEST_LOCATION_CODE = 112;
    private static final String TAG = FirstFragment.class.getSimpleName();
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();
        MapsInitializer.initialize(getActivity());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                accessLocations();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void accessLocations() {
        LocationManager lm;
        Location location;
        //Geocoder geo;

        //convert lat, long to address.
        //List<Address> addresses;
        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        //get the last known latitude longitude from network
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_REQUEST_LOCATION_CODE);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getActivity(), "requesting permission ", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Log.d(TAG, "already get permission");
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //create an instance of geocoder
            //geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            //try
            //{
            //    addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            //    if(addresses.get(0).getPostalCode() != null)
            //    {
            //You can get Lat, Long or Zip code
            //    }
            //}
            //catch (Exception e)
            //{

            //}
            //Set the marker
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Location").snippet("Your current location"));

            // Move the camera and zoom it to location
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mapView.onResume();
                    accessLocations();
                }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

