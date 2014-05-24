package net.daum.android.map.openapi.sampleapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.daum.android.map.openapi.sampleapp.MockLocationProvider.LatLng;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

public class ScenarioPlayer implements Runnable {
	private static final Handler emptyHandler = new Handler();

	private boolean running;
	private MockLocationProvider locationProvider;
	private Activity activity;
	
	private int intervalMillis = 1000;
	private List<Action> actions = new ArrayList<Action>();
	private int actionIndex = 0;
	
	public ScenarioPlayer(LocationManager manager) {
		this.locationProvider = new MockLocationProvider(manager);
	}
	
	public void initialize(LocationListener listener) {
		this.locationProvider.initialize(listener);
	}
	
	public void add(Action action) {
		actions.add(action);
	}

	public void parseFromFile(AssetManager asset, String file) {
        InputStream inputStream = null;
        BufferedReader reader = null;
		try {
			inputStream = asset.open(file);
	        reader = new BufferedReader(new InputStreamReader(inputStream));
	        
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	        	if (line.contains("#"))
	        		line = line.substring(0, line.indexOf("#"));
	        	line = line.trim();
	        	if (line.isEmpty())
	        		continue;
	        	
	        	ParseAction(line);
	        }
		} catch (Exception e) {
			Log.e("scenario", e.getMessage(), e);
		} finally {
			try { if (inputStream != null) inputStream.close(); } catch (Exception e) {}
			try { if (reader != null) reader.close(); } catch (Exception e) {}
		}
	}
	
	private void ParseAction(String line) {
    	Scanner scanner = new Scanner(line);
		try {
	    	String actionName = scanner.next();
	    	if (actionName.equalsIgnoreCase("mov")) add(ParseMoveLocation(scanner));
	    	else if (actionName.equalsIgnoreCase("ctl")) add(ParseControlActivity(scanner));
	    	else if (actionName.equalsIgnoreCase("interval")) add(ParseChangeInterval(scanner));
	    	else if (actionName.equalsIgnoreCase("stop")) add(new Stop());
		} catch (Exception e) {
			Log.e("command", e.getMessage(), e);
		}
    	scanner.close();
	}
	
	private Action ParseMoveLocation(Scanner scanner) {
		double latitude = scanner.nextDouble();
		double longitude = scanner.nextDouble();
		MoveLocation action = new MoveLocation(new LatLng(latitude, longitude));
		return action;
	}
	
	private Action ParseControlActivity(Scanner scanner) throws ClassNotFoundException {
		String className = scanner.next();
		className = "net.daum.android.map.openapi.sampleapp." + className;
		ControlActivity action = new ControlActivity(Class.forName(className));
		return action;
	}
	
	private Action ParseChangeInterval(Scanner scanner) {
		return new ChangeInterval(scanner.nextInt());
	}
	
	
	interface Action {
		public void execute();
	}
	
	class MoveLocation implements Action {
		private LatLng latlng;
		
		public MoveLocation(LatLng latlng) {
			this.latlng = latlng;
		}

		@Override
		public void execute() {
			locationProvider.move(latlng);
		}
	}
	
	class ControlActivity implements Action {
		public Class<?> activityClass;
		
		public ControlActivity(Class<?> activityClass) {
			this.activityClass = activityClass;
		}

		@Override
		public void execute() {
			Intent intent = new Intent(activity, activityClass);
			activity.startActivity(intent);
		}
	}
	
	class ChangeInterval implements Action {
		
		private int newInterval;
		
		public ChangeInterval(int newInterval) {
			super();
			this.newInterval = newInterval;
		}

		@Override
		public void execute() {
			intervalMillis = newInterval;
		}
	}
	
	class Stop implements Action {

		@Override
		public void execute() {
			running = false;
		}
	}

	public void start() {
		if (running) {
			return;
		}
		
		running = true;
	    next();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void stop() {
		running = false;
	}
	
	private void next() {
		if (!running) {
			return;
		}
		
		emptyHandler.postDelayed(this, intervalMillis);
	}

	@Override
	public void run() {
		if (!running) {
			return;
		}
		
		if (++actionIndex >= actions.size()) {
			actionIndex = 0;
		}
		
		actions.get(actionIndex).execute();
		next();
	}
}
