package net.daum.android.map.openapi.sampleapp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DaumMapSampleActivity extends Activity 
				implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener, MapView.POIItemEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
	
	private static final int MENU_HOME = Menu.FIRST + 1;
	private static final int MENU_REPORT = Menu.FIRST + 2;
	private static final int MENU_SETTING = Menu.FIRST + 3;
	
	private static final String LOG_TAG = "DaumMapLibrarySample";
	
	private MapView mapView;
	private MapPOIItem poiItem;
	private MapReverseGeoCoder reverseGeoCoder = null;
	DBTreasureHelper db = new DBTreasureHelper(this);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //db.onUpgrade(db.getWritableDatabase(), 2, 3);
        setTitle("[DaumMapSample]");
        
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        MapView.setMapTilePersistentCacheEnabled(true);
        
        mapView = new MapView(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setShowCurrentLocationMarker(true);
        mapView.setDaumMapApiKey("bdbf3c76fe7abbcfc71a58a91af2cb20b0ee2908");
        mapView.setOpenAPIKeyAuthenticationResultListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setMapType(MapView.MapType.Standard);
        linearLayout.addView(mapView);
        List<Treasure> treasureList = db.getAlltreasure();
        for(Treasure treasure : treasureList)
        	makeMarker(treasure);

        setContentView(linearLayout);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_HOME, Menu.NONE, "").setIcon(R.drawable.button_home_unpushed);
		menu.add(0, MENU_REPORT, Menu.NONE, "").setIcon(R.drawable.button_report_unpushed);
		menu.add(0, MENU_SETTING, Menu.NONE, "").setIcon(R.drawable.button_setting_unpushed);
		addOptionsMenuHackerInflaterFactory();
		return true;
	}
    
    @SuppressWarnings("rawtypes")
    static Class       IconMenuItemView_class = null;
    @SuppressWarnings("rawtypes")
    static Constructor IconMenuItemView_constructor = null;

    // standard signature of constructor expected by inflater of all View classes
    @SuppressWarnings("rawtypes")
    private static final Class[] standard_inflater_constructor_signature = 
    new Class[] { Context.class, AttributeSet.class };
    
    protected void addOptionsMenuHackerInflaterFactory()
    {
        final LayoutInflater infl = getLayoutInflater();

        infl.setFactory(new Factory()
        {
            public View onCreateView(final String name, 
                                     final Context context,
                                     final AttributeSet attrs)
            {
                if (!name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView"))
                    return null; // use normal inflater

                View view = null;

                // "com.android.internal.view.menu.IconMenuItemView" 
                // - is the name of an internal Java class 
                //   - that exists in Android <= 3.2 and possibly beyond
                //   - that may or may not exist in other Android revs
                // - is the class whose instance we want to modify to set background etc.
                // - is the class we want to instantiate with the standard constructor:
                //     IconMenuItemView(context, attrs)
                // - this is what the LayoutInflater does if we return null
                // - unfortunately we cannot just call:
                //     infl.createView(name, null, attrs);
                //   here because on Android 3.2 (and possibly later):
                //   1. createView() can only be called inside inflate(),
                //      because inflate() sets the context parameter ultimately
                //      passed to the IconMenuItemView constructor's first arg,
                //      storing it in a LayoutInflater instance variable.
                //   2. we are inside inflate(),
                //   3. BUT from a different instance of LayoutInflater (not infl)
                //   4. there is no way to get access to the actual instance being used
                // - so we must do what createView() would have done for us
                //
                if (IconMenuItemView_class == null)
                {
                    try
                    {
                        IconMenuItemView_class = getClassLoader().loadClass(name);
                    }
                    catch (ClassNotFoundException e)
                    {
                        // this OS does not have IconMenuItemView - fail gracefully
                        return null; // hack failed: use normal inflater
                    }
                }
                if (IconMenuItemView_class == null)
                    return null; // hack failed: use normal inflater

                if (IconMenuItemView_constructor == null)
                {
                    try
                    {
                        IconMenuItemView_constructor = 
                        IconMenuItemView_class.getConstructor(standard_inflater_constructor_signature);
                    }
                    catch (SecurityException e)
                    {
                        return null; // hack failed: use normal inflater
                    }
                    catch (NoSuchMethodException e)
                    {
                        return null; // hack failed: use normal inflater
                    }
                }
                if (IconMenuItemView_constructor == null)
                    return null; // hack failed: use normal inflater

                try
                {
                    Object[] args = new Object[] { context, attrs };
                    view = (View)(IconMenuItemView_constructor.newInstance(args));
                }
                catch (IllegalArgumentException e)
                {
                    return null; // hack failed: use normal inflater
                }
                catch (InstantiationException e)
                {
                    return null; // hack failed: use normal inflater
                }
                catch (IllegalAccessException e)
                {
                    return null; // hack failed: use normal inflater
                }
                catch (InvocationTargetException e)
                {
                    return null; // hack failed: use normal inflater
                }
                if (null == view) // in theory handled above, but be safe... 
                    return null; // hack failed: use normal inflater


                // apply our own View settings after we get back to runloop
                // - android will overwrite almost any setting we make now
                final View v = view;
                new Handler().post(new Runnable()
                {
                    public void run()
                    {
                        v.setBackgroundColor(Color.argb(200, 234, 223, 202));

                        try
                        {
                            // in Android <= 3.2, IconMenuItemView implemented with TextView
                            // guard against possible future change in implementation
                            TextView tv = (TextView)v;
                            tv.setTextColor(Color.WHITE);
                        }
                        catch (ClassCastException e)
                        {
                            // hack failed: do not set TextView attributes
                        }
                    }
                });

                return view;
            }
        });
    }
       
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		final int itemId = item.getItemId();
		
		switch (itemId) {
		case MENU_HOME:
		{
			String hdMapTile = mapView.isHDMapTileEnabled()? "HD Map Tile Off" : "HD Map Tile On";
			String[] mapTypeMenuItems = { "Standard", "Satellite", "Hybrid", hdMapTile, "Clear Map Tile Cache"};

			Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Map Type");
			dialog.setItems(mapTypeMenuItems, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: // Standard
					{
						mapView.setMapType(MapView.MapType.Standard);
					}
						break;
					case 1: // Satellite
					{
						mapView.setMapType(MapView.MapType.Satellite);
					}
						break;
					case 2: // Hybrid
					{
						mapView.setMapType(MapView.MapType.Hybrid);
					}
						break;
					case 3: // HD Map Tile On/Off
					{
						if (mapView.isHDMapTileEnabled()) {
							mapView.setHDMapTileEnabled(false);
						} else {
							mapView.setHDMapTileEnabled(true);
						}
					}
						break;
					case 4: // Clear Map Tile Cache
					{
						MapView.clearMapTilePersistentCache();
					}
						break;
					}
				}

			});
			dialog.show();
		}
			return true;
		
		case 10:
		{
			String rotateMapMenu = mapView.getMapRotationAngle() == 0.0f? "Rotate Map 60" : "Unrotate Map";
			String[] mapMoveMenuItems = { "Move to", "Zoom to", "Move and Zoom to", "Zoom In", "Zoom Out", rotateMapMenu};
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Map Type");
			dialog.setItems(mapMoveMenuItems, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: // Move to
					{
						mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
					}
						break;
					case 1: // Zoom to
					{
						mapView.setZoomLevel(4, true);
					}
						break;
					case 2: // Move and Zoom to
					{
						mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.41, 126.52), 9, true);
					}
						break;
					case 3: // Zoom In
					{
						mapView.zoomIn(true);
					}
						break;
					case 4: // Zoom Out
					{
						mapView.zoomOut(true);
					}
						break;
					case 5: // Rotate Map 60, Unrotate Map
					{
						if (mapView.getMapRotationAngle() == 0.0f) {
							mapView.setMapRotationAngle(60.0f, true);	
						} else {
							mapView.setMapRotationAngle(0.0f, true);
						}
					}
						break;
					}
				}

			});
			dialog.show();
		}
			return true;
		
		case MENU_REPORT:
		{
			String[] mapMoveMenuItems = { "User Location On", "User Location+Heading On", "Off", "Show Location Marker", "Hide Location Marker", "Reverse Geo-coding"};
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Location Tracking");
			dialog.setItems(mapMoveMenuItems, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: // User Location On
					{
						mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
					}
						break;
					case 1: // User Location+Heading On
					{
						mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
					}
						break;
					case 2: // Off
					{
						mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
					}
						break;
					case 3: // Show Location Marker
					{
						mapView.setShowCurrentLocationMarker(true);
					}
						break;
					case 4: // Hide Location Marker
					{
						if (mapView.isShowingCurrentLocationMarker()) {
							mapView.setShowCurrentLocationMarker(false);	
						}
					}
						break;
					case 5: // Reverse Geo-coding
					{
						reverseGeoCoder = new MapReverseGeoCoder("DAUM_LOCAL_DEMO_APIKEY", mapView.getMapCenterPoint(), DaumMapSampleActivity.this, DaumMapSampleActivity.this);
						reverseGeoCoder.startFindingAddress();
					}
						break;
					}
				}

			});
			dialog.show();
		}
			return true;
			
		case MENU_SETTING:
		{
			String[] overlayMenuItems = { "Add POI Items", "Remove a POI Item", "Remove All POI Items", "Add Polyline1", "Add Polyline2", "Remove All Polylines"};
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Overlay");
			dialog.setItems(overlayMenuItems, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: // Add POI Items
					{
						mapView.removeAllPOIItems();
						
						poiItem = new MapPOIItem();
						poiItem.setItemName("City on a Hill");
						poiItem.setMapPoint(MapPoint.mapPointWithGeoCoord(37.541889,127.095388));
						poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
						poiItem.setShowAnimationType(MapPOIItem.ShowAnimationType.DropFromHeaven);
						mapView.addPOIItem(poiItem);
						
						MapPOIItem poiItem2 = new MapPOIItem();
						poiItem2.setItemName("압구정");
						poiItem2.setMapPoint(MapPoint.mapPointWithGeoCoord(37.527896,127.036245));
						poiItem2.setMarkerType(MapPOIItem.MarkerType.RedPin);
						poiItem2.setShowAnimationType(MapPOIItem.ShowAnimationType.NoAnimation);
//						poiItem2.setShowCalloutBalloonOnTouch(false);
						poiItem2.setTag(153);
						mapView.addPOIItem(poiItem2);
						
						MapPOIItem poiItem3 = new MapPOIItem();
						poiItem3.setItemName("다음커뮤니케이션");
						poiItem3.setUserObject(String.format("item%d", 3));
						poiItem3.setMapPoint(MapPoint.mapPointWithGeoCoord(37.537229,127.005515));
						poiItem3.setMarkerType(MapPOIItem.MarkerType.CustomImage);
						poiItem3.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
						poiItem3.setCustomImageResourceId(R.drawable.custom_poi_marker);
						// if anchor point is not set, the bottom-center point of image will be set as anchor point
						poiItem3.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(22,0));
						mapView.addPOIItem(poiItem3);
						
						MapPOIItem poiItem4 = new MapPOIItem();
						poiItem4.setItemName("서울숲");
						poiItem4.setMapPoint(MapPoint.mapPointWithGeoCoord(37.545024,127.03923));
						poiItem4.setMarkerType(MapPOIItem.MarkerType.YellowPin);
						poiItem4.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
						poiItem4.setShowDisclosureButtonOnCalloutBalloon(false);
						poiItem4.setDraggable(true);
						poiItem4.setTag(276);
						mapView.addPOIItem(poiItem4);
						
						mapView.fitMapViewAreaToShowAllPOIItems();
					}
						break;
					case 1: // Remove a POI Item"
					{
						MapPOIItem poiItem = mapView.findPOIItemByTag(276);
						if (poiItem != null) {
							mapView.removePOIItem(poiItem);	
						}
					}
						break;
					case 2: // Remove All POI Items
					{
						mapView.removeAllPOIItems();
					}
						break;
					case 3: // Add Polyline1
					{
						MapPolyline existingPolyline = mapView.findPolylineByTag(1000);
						if (existingPolyline != null) {
							mapView.removePolyline(existingPolyline);
						}
						
						MapPolyline polyline1 = new MapPolyline();
						polyline1.setTag(1000);
						polyline1.setLineColor(Color.argb(128, 255, 51, 0));						
						polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.537229,127.005515));
						polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.545024,127.03923));
						polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.527896,127.036245));
						polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.541889,127.095388));
						mapView.addPolyline(polyline1);
						
						mapView.fitMapViewAreaToShowPolyline(polyline1);
					}
						break;
					case 4: // Add Polyline2
					{
						MapPOIItem existingPOIItemStart = mapView.findPOIItemByTag(10001);
						if (existingPOIItemStart != null) {
							mapView.removePOIItem(existingPOIItemStart);
						}
						
						MapPOIItem existingPOIItemEnd = mapView.findPOIItemByTag(10002);
						if (existingPOIItemEnd != null) {
							mapView.removePOIItem(existingPOIItemEnd);
						}
						
						MapPolyline existingPolyline = mapView.findPolylineByTag(2000);
						if (existingPolyline != null) {
							mapView.removePolyline(existingPolyline);
						}
						
						MapPOIItem poiItemStart = new MapPOIItem();
						poiItemStart.setItemName("Start");
						poiItemStart.setTag(10001);
						poiItemStart.setMapPoint(MapPoint.mapPointWithWCONGCoord(475334.0,1101210.0));
						poiItemStart.setMarkerType(MapPOIItem.MarkerType.CustomImage);
						poiItemStart.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
						poiItemStart.setShowCalloutBalloonOnTouch(false);
						poiItemStart.setCustomImageResourceId(R.drawable.custom_poi_marker_start);
						poiItemStart.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(29,2));
						mapView.addPOIItem(poiItemStart);
						
						MapPOIItem poiItemEnd = new MapPOIItem();
						poiItemEnd.setItemName("End");
						poiItemEnd.setTag(10001);
						poiItemEnd.setMapPoint(MapPoint.mapPointWithWCONGCoord(485016.0,1118034.0));
						poiItemEnd.setMarkerType(MapPOIItem.MarkerType.CustomImage);
						poiItemEnd.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
						poiItemEnd.setShowCalloutBalloonOnTouch(false);
						poiItemEnd.setCustomImageResourceId(R.drawable.custom_poi_marker_end);
						poiItemEnd.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(29,2));
						mapView.addPOIItem(poiItemEnd);
						
						MapPolyline polyline2 = new MapPolyline(21);
						polyline2.setTag(2000);
						polyline2.setLineColor(Color.argb(128, 0, 0, 255));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(475334.0,1101210.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(474300.0,1104123.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(474300.0,1104123.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(473873.0,1105377.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(473302.0,1107097.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(473126.0,1109606.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(473063.0,1110548.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(473435.0,1111020.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(474068.0,1111714.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(475475.0,1112765.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(476938.0,1113532.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(478725.0,1114391.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(479453.0,1114785.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(480145.0,1115145.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(481280.0,1115237.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(481777.0,1115164.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(482322.0,1115923.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(482832.0,1116322.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(483384.0,1116754.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(484401.0,1117547.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(484893.0,1117930.0));
						polyline2.addPoint(MapPoint.mapPointWithWCONGCoord(485016.0,1118034.0));
						mapView.addPolyline(polyline2);
						
						mapView.fitMapViewAreaToShowAllPolylines();
					}
						break;
					case 5: // Remove All Polylines
					{
						mapView.removeAllPolylines();
					}
						break;
					}
				}

			});
			dialog.show();
		}
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// net.daum.mf.map.api.MapView.OpenAPIKeyAuthenticationResultListener

	@Override
	public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
		Log.i(LOG_TAG,	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));	
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// net.daum.mf.map.api.MapView.MapViewEventListener
	
	public void onMapViewInitialized(MapView mapView) { 
		Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
		//mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
		mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.564045, 126.938110), 2, true);
		mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
	} 
	
	@Override
	public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
		MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
		Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		long row_id;
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				row_id = data.getLongExtra("treasureRowId", 0);
				//Treasure Input Operation
				Toast.makeText(getApplicationContext(), String.valueOf(row_id), Toast.LENGTH_LONG).show();
				Treasure treasure = db.getTreasure(row_id);
				makeMarker(treasure);
				break;

			default:
				break;
			}
		}
	}
	
	public void makeMarker(Treasure treasure) {
		MapPOIItem poiItem = new MapPOIItem();
		poiItem.setItemName(treasure.getName());
		poiItem.setUserObject(String.format("item%d", 2));
		poiItem.setMapPoint(MapPoint.mapPointWithGeoCoord(treasure.getLatitude(), treasure.getLongitude()));
		poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
		poiItem.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
		poiItem.setCustomImageResourceId(R.drawable.custom_poi_marker);
		poiItem.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(22,0));
		poiItem.setShowCalloutBalloonOnTouch(true);
		poiItem.setTag(treasure.getID());
		mapView.addPOIItem(poiItem);
		mapView.fitMapViewAreaToShowAllPOIItems();
	}

	@Override
	public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
		
	}

	@Override
	public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
		Intent intent = new Intent(this, TreasureAlert.class);
		intent.putExtra("latitude", mapPoint.getMapPointGeoCoord().latitude);
		intent.putExtra("longitude", mapPoint.getMapPointGeoCoord().longitude);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
		MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
		Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
	}

	@Override
	public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
		Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// net.daum.mf.map.api.MapView.CurrentLocationEventListener
	
	@Override
	public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
		MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
		Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
	}

	@Override
	public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float headingAngle) {
		Log.i(LOG_TAG, String.format("MapView onCurrentLocationDeviceHeadingUpdate: device heading = %f degrees", headingAngle));
	}
	
	@Override
	public void onCurrentLocationUpdateFailed(MapView mapView) {	
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("DaumMapLibrarySample");
		alertDialog.setMessage("Current Location Update Failed!");
		alertDialog.setPositiveButton("OK", null);
		alertDialog.show();
	}
	
	@Override
	public void onCurrentLocationUpdateCancelled(MapView mapView) {
		Log.i(LOG_TAG, "MapView onCurrentLocationUpdateCancelled");
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// net.daum.mf.map.api.MapView.POIItemEventListener
	
	@Override
	public void onPOIItemSelected(MapView mapView, MapPOIItem poiItem) {
		Log.i(LOG_TAG, String.format("MapPOIItem(%s) is selected", poiItem.getItemName()));
	}
	
	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem poiItem) {
		Intent intent = new Intent(this, MapPOIDetailActivity.class);
		intent.putExtra("treasureID", (long) (poiItem.getTag()));
		startActivity(intent);
	}

	@Override
	public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem poiItem, MapPoint newMapPoint) {
		MapPoint.GeoCoordinate newMapPointGeo = newMapPoint.getMapPointGeoCoord();
		String alertMessage = String.format("Draggable MapPOIItem(%s) has moved to new point (%f,%f)", poiItem.getItemName(), newMapPointGeo.latitude, newMapPointGeo.longitude);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("DaumMapLibrarySample");
		alertDialog.setMessage(alertMessage);
		alertDialog.setPositiveButton("OK", null);
		alertDialog.show();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// net.daum.mf.map.api.MapReverseGeoCoder.ReverseGeoCodingResultListener
	
	@Override
	public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder rGeoCoder, String addressString) {
		
		String alertMessage = String.format("Center Point Address = [%s]", addressString);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("DaumMapLibrarySample");
		alertDialog.setMessage(alertMessage);
		alertDialog.setPositiveButton("OK", null);
		alertDialog.show();
		
		reverseGeoCoder = null;
	}
	
	@Override
	public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder rGeoCoder) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("DaumMapLibrarySample");
		alertDialog.setMessage("Reverse Geo-Coding Failed");
		alertDialog.setPositiveButton("OK", null);
		alertDialog.show();
		
		reverseGeoCoder = null;
	}

}