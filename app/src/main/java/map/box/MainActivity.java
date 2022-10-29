package map.box;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import map.box.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityMainBinding binding;
    private SupportMapFragment mapView;
    private GoogleMap map;
    private LatLng latLngIntent;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);

        mapView.getMapAsync(this);

        double lat = getIntent().getDoubleExtra("LAT", 0.0);
        double lng = getIntent().getDoubleExtra("LNG", 0.0);
        latLngIntent = new LatLng(lat, lng);


        binding.imgMyLocation.setOnClickListener(view -> {
            client = LocationServices.getFusedLocationProviderClient(this);
            LocationRequest locationRequest = new LocationRequest.Builder(2000).setWaitForAccurateLocation(false).setMaxUpdates(2000).setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY).build();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Akses lokasi dibuktuhkan!", Toast.LENGTH_SHORT).show();
            } else {
                LocationCallback callback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if(locationResult.getLocations().size() > 0) {
                            Location location = locationResult.getLocations().get(0);
                            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            client.removeLocationUpdates(this);
                        }
                    }
                };

               client.requestLocationUpdates(locationRequest, callback, Looper.myLooper());
            }
        });

        binding.bottonLayout.buttonPilih.setOnClickListener(view -> {
            Helper.setLatLng(map.getCameraPosition().target);
            finish();
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if(latLngIntent != null) {
            map.moveCamera(CameraUpdateFactory.newLatLng(latLngIntent));
        }
        map.setMinZoomPreference(15.0f);
        map.setOnCameraIdleListener(() -> {
            LatLng latLng = map.getCameraPosition().target;
            double lat = latLng.latitude;
            double lng = latLng.longitude;

            try {
                List<Address> add = new Geocoder(this).getFromLocation(lat, lng, 2);
                if (add.size() > 0) {
                    binding.bottonLayout.textLng.setText(new StringBuilder().append("Kecamatan : ").append(add.get(0).getLocality()).toString());
                    binding.bottonLayout.textLat.setText(new StringBuilder().append("Desa : ").append(add.get(0).getSubLocality()).toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}