package org.gexron.location;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.util.List;
import java.util.logging.Logger;

public class LocationUtils {

    private LocationUtils() {

    }

    private static final Logger logger = Logger.getAnonymousLogger();

    private static final double MIN_LATITUDE = 30.028390165683653;
    private static final double MAX_LATITUDE = 30.02947049999885;
    private static final double MIN_LONGITUDE = 31.45432572490963;
    private static final double MAX_LONGITUDE = 31.455802410760185;

    private static final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyCQcUrTtqdqnhawc2GGHSQpN2ueTtvt7jY")
            .build();

    public static boolean isTripCompanyRelated(List<String> tripEndpoints) {
        return (isCompanyApproximateLocation(tripEndpoints.get(0))
                || isCompanyApproximateLocation(tripEndpoints.get(1)));
    }

    private static boolean isCompanyApproximateLocation(String tripEndpoint) {
        double[] latitudeAndLongitude = getLongitudeAndLatitude(tripEndpoint);
        return (latitudeAndLongitude[0] >= MIN_LATITUDE
                && latitudeAndLongitude[0] <= MAX_LATITUDE
                && latitudeAndLongitude[1] >= MIN_LONGITUDE
                && latitudeAndLongitude[1] <= MAX_LONGITUDE);
    }

    private static double[] getLongitudeAndLatitude(String address) {
        GeocodingResult[] results = null;
        try {
            results = GeocodingApi.geocode(context, address).await();
            LatLng location = results[0].geometry.location;
            logger.info("Latitude: " + location.lat + ", Longitude: " + location.lng);
            return new double[]{location.lat, location.lng};
        } catch (Exception e) {
            return new double[]{MAX_LATITUDE, MAX_LONGITUDE};
        }
    }
}
