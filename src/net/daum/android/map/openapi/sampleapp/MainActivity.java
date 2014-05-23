package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle("Android Daum Map Library Sample");
        setContentView(R.layout.main);
        
        Button button1 = (Button)findViewById(R.id.button_start);
        button1.setOnClickListener(buttonClickListener1);
    }
    
    private OnClickListener buttonClickListener1 = new OnClickListener() {
        public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, DaumMapSampleActivity.class);
			startActivity(intent);
        }
    };
}
