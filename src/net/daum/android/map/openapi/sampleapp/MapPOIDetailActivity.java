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
        TextView textComment = (TextView) this.findViewById(R.id.textComment);
        TextView textPhone = (TextView) this.findViewById(R.id.textPhone);
        
        textName.setText(treasure.getName());
        textComment.setText(treasure.getComment());
        textPhone.setText(treasure.getPhoneNumber());
        
        ImageView imageTreasure = (ImageView) this.findViewById(R.id.imageTreasure);
        Bitmap treasureImage = BitmapFactory.decodeByteArray(treasure.getImage(), 0, treasure.getImage().length);  
        imageTreasure.setImageBitmap(treasureImage);
    }
}
