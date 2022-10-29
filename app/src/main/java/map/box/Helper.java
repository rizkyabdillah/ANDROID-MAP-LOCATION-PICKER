package map.box;

import com.google.android.gms.maps.model.LatLng;

public class Helper {

    private static LatLng latLng;

    public static LatLng getLatLng() {
        return latLng;
    }

    public static void setLatLng(LatLng latLng) {
        Helper.latLng = latLng;
    }
}
