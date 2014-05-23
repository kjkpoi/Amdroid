package net.daum.android.map.openapi.sampleapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTreasureHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "treasuresManager";

	// treasure table name
	private static final String TABLE_TREASURE = "treasures";

	// treasure Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_COMMENT = "comment";
	private static final String KEY_PH_NO = "phone_number";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_IMAGE = "image";

	public DBTreasureHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TREASURE_TABLE = "CREATE TABLE " + TABLE_TREASURE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_COMMENT + " TEXT," + KEY_PH_NO + " TEXT," + KEY_LATITUDE + 
				" DOUBLE," + KEY_LONGITUDE + " DOUBLE," + KEY_IMAGE + " BLOB" + ")";
		db.execSQL(CREATE_TREASURE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREASURE);

		// Create tables again
		onCreate(db);
	}

	/**
	 * CRUD 함수
	 */

	// 새로운 Treasure 함수 추가
	public long addTreasure(Treasure treasure) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, treasure.getName());
		values.put(KEY_COMMENT, treasure.getComment());
		values.put(KEY_PH_NO, treasure.getPhoneNumber());
		values.put(KEY_LATITUDE, treasure.getLatitude());
		values.put(KEY_LONGITUDE, treasure.getLongitude());
		values.put(KEY_IMAGE, treasure.getImage());
		long row_id;
		// Inserting Row
		row_id = db.insert(TABLE_TREASURE, null, values);
		db.close();
		return row_id;
	}

	// id 에 해당하는 Treasure 객체 가져오기
	public Treasure getTreasure(long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TREASURE, new String[] { KEY_ID,
				KEY_NAME, KEY_COMMENT, KEY_PH_NO, KEY_LATITUDE, KEY_LONGITUDE, KEY_IMAGE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Treasure treasure = new Treasure(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getBlob(6));
		return treasure;
	}

	// 모든 Treasure 정보 가져오기
	public List<Treasure> getAlltreasure() {
		List<Treasure> treasureList = new ArrayList<Treasure>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TREASURE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Treasure treasure = new Treasure();
				treasure.setID(Integer.parseInt(cursor.getString(0)));
				treasure.setName(cursor.getString(1));
				treasure.setComment(cursor.getString(2));
				treasure.setPhoneNumber(cursor.getString(3));
				treasure.setLatitude(cursor.getDouble(4));
				treasure.setLongitude(cursor.getDouble(5));
				treasure.setImage(cursor.getBlob(6));
				treasureList.add(treasure);
			} while (cursor.moveToNext());
		}

		return treasureList;
	}

	// treasure 정보 업데이트
	public int updateTreasure(Treasure treasure) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, treasure.getName());
		values.put(KEY_COMMENT, treasure.getComment());
		values.put(KEY_PH_NO, treasure.getPhoneNumber());
		values.put(KEY_LATITUDE, treasure.getLatitude());
		values.put(KEY_LONGITUDE, treasure.getLongitude());
		values.put(KEY_IMAGE, treasure.getImage());
		// updating row
		return db.update(TABLE_TREASURE, values, KEY_ID + " = ?",
				new String[] { String.valueOf(treasure.getID()) });
	}

	// treasure 정보 삭제하기
	public void deleteTreasure(Treasure treasure) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TREASURE, KEY_ID + " = ?",
				new String[] { String.valueOf(treasure.getID()) });
		db.close();
	}

	// treasure 정보 숫자
	public int getTreasureCount() {
		String countQuery = "SELECT  * FROM " + TABLE_TREASURE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

}