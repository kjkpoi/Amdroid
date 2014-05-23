package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MapPOIDetailActivity extends Activity {

	DBTreasureHelper db = new DBTreasureHelper(this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle("POI Item Detail");
        
        setContentView(R.layout.poi_detail);
        
        Intent intent = getIntent();
        long treasureID = intent.getLongExtra("treasureID", -1);
        Treasure treasure = db.getTreasure(treasureID);
        TextView textName = (TextView) this.findViewById(R.id.textName);
        TextView textSerial = (TextView) this.findViewById(R.id.textSerial);
        TextView textOwner = (TextView) this.findViewById(R.id.textOwner);
        TextView textDate = (TextView) this.findViewById(R.id.textDate);
        TextView textLevel = (TextView) this.findViewById(R.id.textLevel);
        TextView textSize = (TextView) this.findViewById(R.id.textSize);
        TextView textMessage = (TextView) this.findViewById(R.id.textMessage);
        
        textName.setText(treasure.getName());
        
        ImageView imageTreasure = (ImageView) this.findViewById(R.id.imageTreasure);
        Bitmap treasureImage = BitmapFactory.decodeByteArray(treasure.getImage(), 0, treasure.getImage().length);  
        imageTreasure.setImageBitmap(treasureImage);
    }
}
