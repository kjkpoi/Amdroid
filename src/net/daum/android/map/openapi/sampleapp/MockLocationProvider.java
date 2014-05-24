package net.daum.android.map.openapi.sampleapp;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class MockLocationProvider {
	
	private String provider;
	private LocationManager locationManager;
	
	public MockLocationProvider(LocationManager manager) {
		this(LocationManager.GPS_PROVIDER, manager);
	}
	
	public MockLocationProvider(String provider, LocationManager manager) {
		this.provider = provider;
		this.locationManager = manager;
	}
	
	public void initialize(LocationListener listener) {
	    locationManager.addTestProvider(provider, false, false,
	            false, false, true, true, true, 0, 5);
	    locationManager.setTestProviderEnabled(provider, true);
	    if (listener != null) {
	    	locationManager.requestLocationUpdates(provider, 0, 0, listener);
	    }
	}
	
	public void move(LatLng latlng) {
		Location location = new Location(provider);
		location.setLatitude(latlng.latitude);
		location.setLongitude(latlng.longitude);
		location.setTime(System.currentTimeMillis());

		locationManager.setTestProviderLocation(provider, location);
	}
	
	public static class LatLng {
		public double latitude;
		public double longitude;
		
		public LatLng(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}
}
