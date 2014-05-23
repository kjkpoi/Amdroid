package net.daum.android.map.openapi.sampleapp;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.os.Bundle;

public class DaumMapSampleActivityLayoutXML extends Activity implements MapView.MapViewEventListener {
	
	private MapView mapView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle("Layout XML Sample");
        
        setContentView(R.layout.daum_map_sample);
        
        mapView = (MapView)findViewById(R.id.daumMapView);
        mapView.setDaumMapApiKey("bdbf3c76fe7abbcfc71a58a91af2cb20b0ee2908");
        mapView.setMapViewEventListener(this);
    }

	@Override
	public void onMapViewInitialized(MapView mapView) {
		// MapView had loaded. Now, MapView APIs could be called safely.
		mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.41, 126.52), 9, true); 
	}

	@Override
	public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
		
	}

	@Override
	public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
		
	}

	@Override
	public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
		
	}

	@Override
	public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
		
	}

	@Override
	public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
		
	}
}
