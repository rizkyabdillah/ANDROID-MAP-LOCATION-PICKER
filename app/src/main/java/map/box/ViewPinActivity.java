package map.box;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import map.box.databinding.ActivityViewPinBinding;

public class ViewPinActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityViewPinBinding binding;
    private SupportMapFragment mapView;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapView.getMapAsync(this);

        binding.button.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("LAT", map.getCameraPosition().target.latitude);
            intent.putExtra("LNG", map.getCameraPosition().target.longitude);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;
        map.getUiSettings().setAllGesturesEnabled(false);
        LatLng latLng =new LatLng(-7.789926931660899, 113.34142112978714);
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.setMinZoomPreference(15.0f);
        map.setOnCameraIdleListener(() -> {

        });
    }

    @Override
    protected void onResume() {
        if(Helper.getLatLng() != null) {
            map.moveCamera(CameraUpdateFactory.newLatLng(Helper.getLatLng()));
        }
        super.onResume();
    }
}