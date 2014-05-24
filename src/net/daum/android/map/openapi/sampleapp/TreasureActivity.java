package net.daum.android.map.openapi.sampleapp;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class TreasureActivity extends Activity {

	EditText treasureNameInputBox = null;
	EditText treasureSerialInputBox = null;
	EditText treasureOwnerInputBox = null;
	EditText treasureDateInputBox = null;
	EditText treasureCommentInputBox = null;
	Spinner treasureLevelSpinner = null;
	Spinner treasureSizeSpinner = null;
	DBTreasureHelper db = new DBTreasureHelper(this);
	
	ImageButton treasureImageButton = null;
	Bitmap resizedImage = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.treasure_select);
		treasureNameInputBox = (EditText)this.findViewById(R.id.treasureNameInputBox);
		treasureSerialInputBox = (EditText)this.findViewById(R.id.treasureSerialInputBox);
		treasureOwnerInputBox = (EditText)this.findViewById(R.id.treasureOwnerInputBox);
		treasureDateInputBox = (EditText)this.findViewById(R.id.treasureDateInputBox);
		treasureCommentInputBox = (EditText)this.findViewById(R.id.treasureCommentInputBox);
		treasureLevelSpinner = (Spinner)this.findViewById(R.id.treasureLevelSpinner);
		treasureSizeSpinner = (Spinner)this.findViewById(R.id.treasureSizeSpinner);
		
		String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		treasureDateInputBox.setText(today);
		
		Button submitButton = (Button)this.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(submitButtonClickListener);
		
		treasureImageButton = (ImageButton)this.findViewById(R.id.treasureImageInputButton);
		treasureImageButton.setOnClickListener(imageButtonClickListener);
	}
	
	private OnClickListener submitButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = getIntent();
			double latitude = intent.getDoubleExtra("latitude", 0.0);
			double longitude = intent.getDoubleExtra("longitude", 0.0);
			
			byte[] byteArray = null;
			if(resizedImage != null) {
			    ByteArrayOutputStream stream = new ByteArrayOutputStream();  
		        resizedImage.compress(CompressFormat.JPEG, 100, stream) ;  
		        byteArray = stream.toByteArray() ;  
			}
	    
        	long row_id = db.addTreasure(new Treasure(
        			treasureNameInputBox.getText().toString(),
        			treasureSerialInputBox.getText().toString(),
        			treasureCommentInputBox.getText().toString(),
        			treasureOwnerInputBox.getText().toString(),
        			treasureDateInputBox.getText().toString(),
        			treasureLevelSpinner.getSelectedItem().toString(),
        			treasureSizeSpinner.getSelectedItem().toString(),
        			latitude, longitude, byteArray));
			intent.putExtra("treasureRowId", row_id);
			setResult(RESULT_OK, intent);
			
			startActivityForResult(new Intent(getApplicationContext(), NewTreasureActivity.class), REQUEST_CODE_SHOW_DIALOG);
        }
    };
	
    private OnClickListener imageButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	Intent intent = new Intent(Intent.ACTION_GET_CONTENT) ;
        	intent.setType("image/*");
        	startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
     /*   	 
        	// Button - 카메라
        	if( v == btnCamera )
        	{
        	// 카메라 실행
        	Intent it = new Intent( ) ;
        	 
        	// 사진 저장 경로를 바꾸기
        	File file = new File( Environment.getExternalStorageDirectory( ), 저장될곳+ "/" + "저장될이름" ) ;
        	tempPictuePath = file.getAbsolutePath( ) ;
        	it.putExtra( MediaStore.EXTRA_OUTPUT, tempPictuePath ) ;
        	it.setAction( MediaStore.ACTION_IMAGE_CAPTURE ) ; // 모든 단말에서 안될 수 있기 때문에 수정해야 함
        	 
        	startActivityForResult( it, TAKE_CAMERA ) ;
        	}*/
        }
    };
    
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private static final int REQUEST_CODE_SHOW_DIALOG = 2;
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case REQUEST_CODE_SELECT_IMAGE:
    		try {
        		Uri imgUri = data.getData();
				Bitmap photo = Images.Media.getBitmap(getContentResolver(), imgUri);
				int height = photo.getHeight();
				int width = photo.getWidth();
				int targetHeight = (int) (treasureImageButton.getHeight() * 0.9);
				int targetWidth = (int) (treasureImageButton.getWidth() * 0.9);
				resizedImage = null;
				while(height > targetHeight) {	
					resizedImage = Bitmap.createScaledBitmap(photo, (width * targetHeight) / height, targetHeight, true);
					height = resizedImage.getHeight();
					width  = resizedImage.getWidth();
				}
				
				while(width > targetWidth) {	
					resizedImage = Bitmap.createScaledBitmap(photo, targetWidth, (height * targetWidth) / width, true);
					height = resizedImage.getHeight();
					width  = resizedImage.getWidth();
				}
				
				treasureImageButton.setImageBitmap(resizedImage);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		break;
    	
    	case REQUEST_CODE_SHOW_DIALOG:
    		finish();
    		break;
    	}
    };
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
