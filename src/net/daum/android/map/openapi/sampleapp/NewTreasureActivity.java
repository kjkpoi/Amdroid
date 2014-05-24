package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class NewTreasureActivity extends Activity {

    private final int AutoTransitionDelay = 4000;
    private ImageView imageNotice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_treasure);

		// image를 click하거나 지정된 시간이 지나면 본 activity가 finish가 되고,
		// 따라서 순차적으로 TreasureActivity, TreasureAlert가 finish되면서 추가된 정보를 intent를 통해 DaumMapSampleActivity까지 전달한다.
		imageNotice = (ImageView)this.findViewById(R.id.imageNotice);
		imageNotice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
            }
        }, AutoTransitionDelay);
	}
}
