package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class TreasureAlert extends Activity {

	ImageView notice = null;
	
	Context context = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		setContentView(R.layout.activity_treasure_alert);
		notice = (ImageView) this.findViewById(R.id.imageNotice);
		notice.setOnTouchListener(imageTouchListener);
	}
	
	private OnTouchListener imageTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			float x = event.getX();
			float y = event.getY();
			if(x >= 60 && x <= 440) { 
				if(y >= 465 && y <= 510) {
					Intent getIntent = getIntent();
					double latitude = getIntent.getDoubleExtra("latitude", 0.0);
					double longitude = getIntent.getDoubleExtra("longitude", 0.0);
					
					Intent pushIntent = new Intent(context, TreasureActivity.class);
					pushIntent.putExtra("latitude", latitude);
					pushIntent.putExtra("longitude", longitude);
					startActivityForResult(pushIntent, 1);
				} else if(y >= 530 && y <= 585) {
					Toast.makeText(getApplicationContext(), "Push No", Toast.LENGTH_LONG).show();
				}
			}
			return false;
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		long row_id;
		if(resultCode == RESULT_OK) {
			Intent intent = getIntent();
			switch (requestCode) {
			case 1:
				row_id = data.getLongExtra("treasureRowId", 0);
				intent.putExtra("treasureRowId", row_id);
				setResult(RESULT_OK, intent);
				finish();	
				break;

			default:
				break;
			}
		}
	}; 
}
